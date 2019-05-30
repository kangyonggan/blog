package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Menu;
import com.kangyonggan.blog.service.system.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("api/system/menu")
@Api(tags = "ApiSystemMenuController", description = "菜单管理相关接口")
public class ApiSystemMenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    /**
     * 菜单列表查询
     *
     * @return
     */
    @GetMapping
    @ApiOperation("菜单列表查询")
    @PermissionMenu("SYSTEM_MENU")
    public Response list() {
        Response response = successResponse();
        List<Menu> menus = menuService.findAllMenus();

        response.put("menus", menus);
        return response;
    }

    /**
     * 保存菜单
     *
     * @return
     */
    @PostMapping("save")
    @ApiOperation("保存菜单")
    @PermissionMenu("SYSTEM_MENU")
    public Response save(Menu menu) {
        menuService.saveMenu(menu);
        return successResponse();
    }

    /**
     * 更新菜单
     *
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新菜单")
    @PermissionMenu("SYSTEM_MENU")
    public Response update(Menu menu) {
        menuService.updateMenu(menu);
        return successResponse();
    }

    /**
     * 删除菜单
     *
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除菜单")
    @PermissionMenu("SYSTEM_MENU")
    @ApiImplicitParam(name = "menuId", value = "菜单ID", required = true, example = "2")
    public Response delete(Long menuId) {
        menuService.deleteMenu(menuId);
        return successResponse();
    }

}
