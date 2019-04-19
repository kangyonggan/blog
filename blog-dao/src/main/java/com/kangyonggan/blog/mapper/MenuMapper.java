package com.kangyonggan.blog.mapper;

import com.kangyonggan.blog.MyMapper;
import com.kangyonggan.blog.model.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kangyonggan
 * @since 8/8/18
 */
public interface MenuMapper extends MyMapper<Menu> {

    /**
     * 判断用户是否拥有某菜单
     *
     * @param userId
     * @param menuCodes
     * @return
     */
    boolean selectExistsUserMenuCodes(@Param("userId") Long userId, @Param("menuCodes") String[] menuCodes);

    /**
     * 查找用户菜单
     *
     * @param userId
     * @return
     */
    List<Menu> selectMenusByUserId(Long userId);

    /**
     * 查找角色菜单
     *
     * @param roleId
     * @return
     */
    List<Menu> selectMenusByRoleId(Long roleId);

    /**
     * 删除角色所有菜单
     *
     * @param roleId
     */
    void deleteAllMenusByRoleId(Long roleId);
}