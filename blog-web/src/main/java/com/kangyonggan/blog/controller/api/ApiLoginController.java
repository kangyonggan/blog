package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.annotation.PermissionLogin;
import com.kangyonggan.blog.constants.AppConstants;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.interceptor.ParamsInterceptor;
import com.kangyonggan.blog.model.Menu;
import com.kangyonggan.blog.model.User;
import com.kangyonggan.blog.service.system.MenuService;
import com.kangyonggan.blog.service.system.UserService;
import com.kangyonggan.blog.util.Digests;
import com.kangyonggan.blog.util.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-27
 */
@RestController
@RequestMapping("api")
@Log4j2
@Api(tags = "ApiLoginController", description = "登录、登出相关接口")
public class ApiLoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    /**
     * 登录
     *
     * @param email
     * @param password
     * @return
     */
    @PostMapping("login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "电子邮箱", required = true, example = "admin@kangyonggan.com"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, example = "11111111")
    })
    public Response login(String email, String password) {
        Response response = successResponse();

        User user = userService.findUserByEmail(email);
        if (user == null) {
            return response.failure("8001", "账号不存在");
        }
        if (user.getIsDeleted().equals(YesNo.YES.getCode())) {
            return response.failure("8002", "账号已删除");
        }

        byte[] salt = Encodes.decodeHex(user.getSalt());
        byte[] hashPassword = Digests.sha1(password.getBytes(), salt, AppConstants.HASH_INTERATIONS);
        String realPassword = Encodes.encodeHex(hashPassword);
        if (!user.getPassword().equals(realPassword)) {
            log.error("密码错误, user:{}", user);
            return response.failure("密码错误");
        }

        // 把登录信息放入session
        HttpSession session = ParamsInterceptor.getSession();
        session.setAttribute(AppConstants.KEY_SESSION_USER, user);
        log.info("登录成功,sessionId:{}", session.getId());
        response.put(AppConstants.HEADER_TOKEN_NAME, session.getId());

        return response;
    }

    /**
     * 获取用户数据
     *
     * @return
     */
    @GetMapping("userData")
    @PermissionLogin
    @ApiOperation("获取用户数据")
    public Response userData() {
        Response response = successResponse();
        response.put("user", userService.findUserDtoById(currentUserId()));
        return response;
    }

    /**
     * 获取用户菜单
     *
     * @return
     */
    @GetMapping("menus")
    @PermissionLogin
    @ApiOperation("获取用户菜单")
    public Response menus() {
        Response response = successResponse();
        List<Menu> menus = menuService.findMenusByUserId(currentUserId());
        response.put("menus", menus);
        return response;
    }

    /**
     * 登出
     *
     * @return
     */
    @GetMapping("logout")
    @PermissionLogin
    @ApiOperation("登出")
    public Response logout() {
        log.info("登出成功,sessionId:{}", ParamsInterceptor.getSession().getId());
        ParamsInterceptor.getSession().invalidate();
        return successResponse();
    }
}
