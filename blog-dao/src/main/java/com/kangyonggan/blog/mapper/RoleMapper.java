package com.kangyonggan.blog.mapper;

import com.kangyonggan.blog.MyMapper;
import com.kangyonggan.blog.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kangyonggan
 * @since 8/8/18
 */
public interface RoleMapper extends MyMapper<Role> {

    /**
     * 判断用户是否拥有某角色
     *
     * @param userId
     * @param roleCodes
     * @return
     */
    boolean selectExistsUserRoleCode(@Param("userId") Long userId, @Param("roleCodes") String[] roleCodes);

    /**
     * 删除用户所有角色
     *
     * @param userId
     */
    void deleteAllRolesByUserId(@Param("userId") Long userId);

    /**
     * 查找用户所有角色
     *
     * @param userId
     * @return
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 添加角色菜单
     *
     * @param roleId
     * @param menuIds
     */
    void insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") String[] menuIds);
}