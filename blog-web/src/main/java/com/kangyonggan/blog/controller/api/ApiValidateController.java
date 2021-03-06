package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.annotation.PermissionLogin;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.service.system.DictService;
import com.kangyonggan.blog.service.system.MenuService;
import com.kangyonggan.blog.service.system.RoleService;
import com.kangyonggan.blog.service.system.UserService;
import com.kangyonggan.blog.util.IdNoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @Autowired
    private MenuService menuService;

    @Autowired
    private DictService dictService;

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

    /**
     * 校验菜单代码是否存在
     *
     * @param menuCode
     * @return
     */
    @PostMapping("menu")
    @PermissionLogin
    @ApiOperation("校验菜单代码是否存在")
    @ApiImplicitParam(name = "menuCode", value = "菜单代码", required = true, example = "SYSTEM_USER")
    public Response menu(String menuCode) {
        Response response = successResponse();

        if (menuService.existsMenuCode(menuCode)) {
            response.failure("菜单代码已存在");
        }

        return response;
    }

    /**
     * 校验字典代码是否存在
     *
     * @param dictType
     * @param dictCode
     * @return
     */
    @PostMapping("dict")
    @PermissionLogin
    @ApiOperation("校验字典代码是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, example = "NAV"),
            @ApiImplicitParam(name = "dictCode", value = "字典代码", required = true, example = "/article")
    })
    public Response dict(String dictType, String dictCode) {
        Response response = successResponse();

        if (dictService.existsDictCode(dictType, dictCode)) {
            response.failure("字典代码已存在");
        }

        return response;
    }

    /**
     * 校验证件号码是否合法
     *
     * @param idNo
     * @return
     */
    @PostMapping("idNo")
    @ApiOperation("校验证件号码是否合法")
    @ApiImplicitParam(name = "idNo", value = "证件号码", required = true, example = "340321199103173095")
    public Response idNo(String idNo) {
        Response response = successResponse();
        idNo = idNo.replaceAll("x", "X");

        if (!IdNoUtil.isIdCard(idNo)) {
            response.failure("身份证号码不合法");
        }

        return response;
    }

}
