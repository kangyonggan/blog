package com.kangyonggan.blog.service.impl.system;

import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.AppConstants;
import com.kangyonggan.blog.dto.UserDto;
import com.kangyonggan.blog.mapper.UserMapper;
import com.kangyonggan.blog.model.User;
import com.kangyonggan.blog.model.UserProfile;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.system.RoleService;
import com.kangyonggan.blog.service.system.UserProfileService;
import com.kangyonggan.blog.service.system.UserService;
import com.kangyonggan.blog.util.Digests;
import com.kangyonggan.blog.util.Encodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author kangyonggan
 * @since 12/6/18
 */
@Service
@CacheConfig(cacheNames = "blog:user")
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserProfileService userProfileService;

    @Override
    public User findUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return myMapper.selectOne(user);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user, String[] roleIds) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            entryptPassword(user);
        } else {
            updateUserRoles(user.getUserId(), roleIds);
        }
        myMapper.updateByPrimaryKeySelective(user);
    }

    private void updateUserRoles(Long userId, String[] roleIds) {
        roleService.deleteAllRolesByUserId(userId);
        if (roleIds != null && roleIds.length > 0) {
            userMapper.insertUserRoles(userId, roleIds);
        }
    }

    @Override
    public boolean existsEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return super.exists(user);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user, String ipAddress) {
        entryptPassword(user);
        myMapper.insertSelective(user);

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getUserId());
        userProfile.setIpAddress(ipAddress);
        userProfile.setName(user.getEmail().substring(0, user.getEmail().indexOf("@")));
        userProfileService.saveUserProfile(userProfile);
    }

    @Override
    @MethodLog
    public void deleteUser(Long userId) {
        myMapper.deleteByPrimaryKey(userId);
    }

    @Override
    @MethodLog
    public UserDto findUserProfileById(Long userId) {
        return userMapper.selectUserProfileById(userId);
    }

    /**
     * 设定安全的密码，生成随机的salt并经过N次 sha-1 hash
     *
     * @param user
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(AppConstants.SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, AppConstants.HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

}
