package com.kangyonggan.blog.service.system;

import com.kangyonggan.blog.dto.UserRequest;
import com.kangyonggan.blog.model.User;

import java.util.List;

/**
 * @author kangyonggan
 * @since 12/6/18
 */
public interface UserService {

    /**
     * 查找用户
     *
     * @param email
     * @return
     */
    User findUserByEmail(String email);

    /**
     * 更新用户
     *
     * @param user
     */
    void updateUser(User user);

    /**
     * 更新用户角色
     *
     * @param user
     * @param roleIds
     */
    void updateUserRole(User user, String[] roleIds);

    /**
     * 更新用户密码
     *
     * @param user
     */
    void updateUserPassword(User user);

    /**
     * 校验邮箱是否存在
     *
     * @param email
     * @return
     */
    boolean existsEmail(String email);

    /**
     * 保存用户
     *
     * @param user
     * @param ipAddress
     */
    void saveUser(User user, String ipAddress);

    /**
     * 搜索用户
     *
     * @param userRequest
     * @return
     */
    List<User> searchUsers(UserRequest userRequest);
}
