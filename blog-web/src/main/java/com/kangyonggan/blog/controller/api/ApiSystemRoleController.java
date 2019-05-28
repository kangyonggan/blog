package com.kangyonggan.blog.controller.api;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.dto.RoleRequest;
import com.kangyonggan.blog.model.Menu;
import com.kangyonggan.blog.model.Role;
import com.kangyonggan.blog.service.system.MenuService;
import com.kangyonggan.blog.service.system.RoleService;
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
@RequestMapping("api/system/role")
@Api(tags = "ApiSystemRoleController", description = "角色管理相关接口")
public class ApiSystemRoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    /**
     * 角色列表查询
     *
     * @param roleRequest
     * @return
     */
    @PostMapping
    @ApiOperation("角色列表查询")
    @PermissionMenu("SYSTEM_ROLE")
    public Response list(RoleRequest roleRequest) {
        Response response = successResponse();

        List<Role> list = roleService.searchRoles(roleRequest);
        PageInfo<Role> pageInfo = new PageInfo<>(list);

        response.put("pageInfo", pageInfo);
        return response;
    }

    /**
     * 保存角色
     *
     * @param role
     * @return
     */
    @PostMapping("save")
    @PermissionMenu("SYSTEM_ROLE")
    @ApiOperation("保存角色")
    public Response save(Role role) {
        roleService.saveRole(role);

        return successResponse();
    }

    /**
     * 查询角色角菜单
     *
     * @param roleId
     * @return
     */
    @PostMapping("menu")
    @PermissionMenu("SYSTEM_ROLE")
    @ApiOperation("查询角色菜单")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, example = "1")
    public Response menu(Long roleId) {
        Response response = successResponse();
        List<Menu> roleMenus = menuService.findRoleMenus(roleId);
        if (roleMenus != null) {
            roleMenus = Collections3.extractToList(roleMenus, "menuId");
        }

        List<Menu> allMenus = menuService.findAllMenus();

        response.put("roleMenus", roleMenus);
        response.put("allMenus", allMenus);
        return response;
    }

    /**
     * 更新角色
     *
     * @param roleId
     * @param roleCode
     * @param roleName
     * @param menuIds
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新角色")
    @PermissionMenu("SYSTEM_ROLE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, example = "1"),
            @ApiImplicitParam(name = "roleCode", value = "角色代码", required = true, example = "ROLE_ADMIN"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, example = "管理员"),
            @ApiImplicitParam(name = "menuIds", value = "菜单ID", required = true, example = "1,2,3,4,5,6,7,8,9,10")
    })
    public Response update(Long roleId, String roleCode, String roleName, String[] menuIds) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        roleService.updateRoleMenu(role, menuIds);

        return successResponse();
    }

    /**
     * 删除/恢复角色
     *
     * @param roleId
     * @param isDeleted
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除/恢复角色")
    @PermissionMenu("SYSTEM_ROLE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, example = "1"),
            @ApiImplicitParam(name = "isDeleted", value = "是否删除(0:恢复，1:删除)", required = true, example = "0")
    })
    public Response delete(Long roleId, Byte isDeleted) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setIsDeleted(isDeleted);
        roleService.updateRole(role);

        return successResponse();
    }

}
