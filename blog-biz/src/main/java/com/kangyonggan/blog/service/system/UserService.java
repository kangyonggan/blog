package com.kangyonggan.blog.service.system;

import com.kangyonggan.blog.dto.UserDto;
import com.kangyonggan.blog.model.User;

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
     * @param roleIds
     */
    void updateUser(User user, String[] roleIds);

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
     * 删除用户
     *
     * @param userId
     */
    void deleteUser(Long userId);

    /**
     * 查找用户信息
     *
     * @param userId
     * @return
     */
    UserDto findUserProfileById(Long userId);
}
