package com.kangyonggan.blog.service.system;

import com.kangyonggan.blog.model.Role;

import java.util.List;

/**
 * @author kangyonggan
 * @since 12/6/18
 */
public interface RoleService {

    /**
     * 判断用户是否拥有某角色
     *
     * @param userId
     * @param roleCodes
     * @return
     */
    boolean hasRoles(Long userId, String... roleCodes);

    /**
     * 删除用户的所有角色
     *
     * @param userId
     */
    void deleteAllRolesByUserId(Long userId);

    /**
     * 查找所有角色
     *
     * @return
     */
    List<Role> findAllRoles();

    /**
     * 查找用户所有角色
     *
     * @param userId
     * @return
     */
    List<Role> findRolesByUserId(Long userId);

    /**
     * 删除角色
     *
     * @param roleId
     */
    void deleteRole(Long roleId);

    /**
     * 保存角色
     *
     * @param role
     */
    void saveRole(Role role);

    /**
     * 更新角色
     *
     * @param role
     * @param menuIds
     */
    void updateRole(Role role, String[] menuIds);

    /**
     * 判断角色代码是否存在
     *
     * @param roleCode
     * @return
     */
    boolean existsRoleCode(String roleCode);
}
