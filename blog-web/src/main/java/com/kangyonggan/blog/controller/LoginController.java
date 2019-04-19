package com.kangyonggan.blog.controller;

import com.kangyonggan.blog.constants.AppConstants;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.interceptor.ParamsInterceptor;
import com.kangyonggan.blog.model.User;
import com.kangyonggan.blog.service.system.UserService;
import com.kangyonggan.blog.util.Digests;
import com.kangyonggan.blog.util.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 处理登录、登出相关请求
 *
 * @author kangyonggan
 * @since 2019-03-26
 */
@RestController
@RequestMapping("/")
@Log4j2
@Api(tags = "LoginController", description = "登录、登出")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("login")
    @ApiOperation("登录")
    public Response login(@RequestBody User user) {
        Response response = successResponse();

        User dbUser = userService.findUserByEmail(user.getEmail());
        if (dbUser == null) {
            return response.failure("8001", "账号不存在");
        }
        if (dbUser.getIsDeleted().equals(YesNo.YES.getCode())) {
            return response.failure("8002", "账号已锁定");
        }

        byte[] salt = Encodes.decodeHex(dbUser.getSalt());
        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, AppConstants.HASH_INTERATIONS);
        String targetPassword = Encodes.encodeHex(hashPassword);
        if (!dbUser.getPassword().equals(targetPassword)) {
            return response.failure("8003", "密码错误");
        }

        // 把登录信息放入session
        HttpSession session = ParamsInterceptor.getSession();
        session.setAttribute(AppConstants.KEY_SESSION_USER, dbUser);
        log.info("登录成功,sessionId:{}", session.getId());
        return response;
    }

    /**
     * 登出
     *
     * @return
     */
    @GetMapping("logout")
    @ApiOperation("登出")
    public Response logout() {
        log.info("登出成功,sessionId:{}", ParamsInterceptor.getSession().getId());
        ParamsInterceptor.getSession().invalidate();
        return successResponse();
    }

}
