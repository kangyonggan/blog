package com.kangyonggan.blog.controller.api;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.dto.UserRequest;
import com.kangyonggan.blog.model.Role;
import com.kangyonggan.blog.model.User;
import com.kangyonggan.blog.service.system.RoleService;
import com.kangyonggan.blog.service.system.UserService;
import com.kangyonggan.blog.util.Collections3;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("api/system/user")
@Api(tags = "ApiSystemUserController", description = "用户管理相关接口")
public class ApiSystemUserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 用户列表查询
     *
     * @param userRequest
     * @return
     */
    @PostMapping
    @ApiOperation("用户列表查询")
    @PermissionMenu("SYSTEM_USER")
    public Response list(UserRequest userRequest) {
        Response response = successResponse();

        List<User> list = userService.searchUsers(userRequest);
        PageInfo<User> pageInfo = new PageInfo<>(list);

        response.put("pageInfo", pageInfo);
        return response;
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @PostMapping("save")
    @PermissionMenu("SYSTEM_USER")
    @ApiOperation("保存用户")
    public Response save(User user) {
        userService.saveUser(user, getIpAddress());

        return successResponse();
    }

    /**
     * 查询用户角色
     *
     * @param userId
     * @return
     */
    @PostMapping("role")
    @PermissionMenu("SYSTEM_USER")
    @ApiOperation("查询用户角色")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, example = "1")
    public Response role(Long userId) {
        Response response = successResponse();
        List<Role> allRoles = roleService.findAllRoles();
        List<Role> userRoles = roleService.findRolesByUserId(userId);

        response.put("allRoles", allRoles);
        response.put("userRoles", Collections3.extractToList(userRoles, "roleId"));
        return response;
    }

    /**
     * 更新用户
     *
     * @param userId
     * @param email
     * @param roleIds
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新用户")
    @PermissionMenu("SYSTEM_USER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, example = "1"),
            @ApiImplicitParam(name = "email", value = "电子邮箱", required = true, example = "app@kangyonggan.com"),
            @ApiImplicitParam(name = "roleIds", value = "角色ID", required = true, example = "1,2")
    })
    public Response update(Long userId, String email, String[] roleIds) {
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        userService.updateUserRole(user, roleIds);

        return successResponse();
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param password
     * @return
     */
    @PostMapping("password")
    @ApiOperation("修改密码")
    @PermissionMenu("SYSTEM_USER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, example = "1"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, example = "11111111")
    })
    public Response password(Long userId, String password) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        userService.updateUserPassword(user);

        return successResponse();
    }

    /**
     * 删除/恢复用户
     *
     * @param userId
     * @param isDeleted
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除/恢复用户")
    @PermissionMenu("SYSTEM_USER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, example = "1"),
            @ApiImplicitParam(name = "isDeleted", value = "是否删除(0:恢复，1:删除)", required = true, example = "0")
    })
    public Response delete(Long userId, Byte isDeleted) {
        User user = new User();
        user.setUserId(userId);
        user.setIsDeleted(isDeleted);
        userService.updateUser(user);

        return successResponse();
    }

}
