package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.annotation.PermissionLogin;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.service.system.RoleService;
import com.kangyonggan.blog.service.system.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("api/validate")
@Api(tags = "ApiValidateController", description = "校验相关接口")
public class ApiValidateController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 校验电子邮箱是否存在
     *
     * @param email
     * @return
     */
    @PostMapping("email")
    @PermissionLogin
    @ApiOperation("校验电子邮箱是否存在")
    @ApiImplicitParam(name = "email", value = "电子邮箱", required = true, example = "admin@kangyonggan.com")
    public Response email(String email) {
        Response response = successResponse();

        if (userService.existsEmail(email)) {
            response.failure("电子邮箱已存在");
        }

        return response;
    }

    /**
     * 校验角色代码是否存在
     *
     * @param roleCode
     * @return
     */
    @PostMapping("role")
    @PermissionLogin
    @ApiOperation("校验角色代码是否存在")
    @ApiImplicitParam(name = "roleCode", value = "角色代码", required = true, example = "ROLE_ADMIN")
    public Response role(String roleCode) {
        Response response = successResponse();

        if (roleService.existsRoleCode(roleCode)) {
            response.failure("角色代码已存在");
        }

        return response;
    }

}
