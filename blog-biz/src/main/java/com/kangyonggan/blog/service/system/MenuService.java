package com.kangyonggan.blog.service.system;


import com.kangyonggan.blog.model.Menu;

import java.util.List;

/**
 * @author kangyonggan
 * @since 12/6/18
 */
public interface MenuService {

    /**
     * 判断用户是否拥有某菜单
     *
     * @param userId
     * @param menuCodes
     * @return
     */
    boolean hasMenus(Long userId, String... menuCodes);

    /**
     * 查找用户菜单
     *
     * @param userId
     * @return
     */
    List<Menu> findMenusByUserId(Long userId);

    /**
     * 查询全部菜单
     *
     * @return
     */
    List<Menu> findAllMenus();

    /**
     * 获取角色菜单
     *
     * @param roleId
     * @return
     */
    List<Menu> findMenusByRoleId(Long roleId);

    /**
     * 删除角色所有菜单
     *
     * @param roleId
     */
    void deleteAllMenusByRoleId(Long roleId);

    /**
     * 删除菜单
     *
     * @param menuId
     */
    void deleteMenu(Long menuId);

    /**
     * 保存菜单
     *
     * @param menu
     */
    void saveMenu(Menu menu);

    /**
     * 更新菜单
     *
     * @param menu
     */
    void updateMenu(Menu menu);

    /**
     * 校验菜单代码是否存在
     *
     * @param menuCode
     * @return
     */
    boolean existsMenuCode(String menuCode);

    /**
     * 查找角色菜单
     *
     * @param roleId
     * @return
     */
    List<Menu> findRoleMenus(Long roleId);
}
