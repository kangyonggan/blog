package com.kangyonggan.blog.service.impl.system;

import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.model.UserProfile;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.system.UserProfileService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @author kangyonggan
 * @since 2018/12/9 0009
 */
@Service
public class UserProfileServiceImpl extends BaseService<UserProfile> implements UserProfileService {

    @Override
    @MethodLog
    public void updateUserProfile(UserProfile userProfile) {
        Example example = new Example(UserProfile.class);
        example.createCriteria().andEqualTo("userId", userProfile.getUserId());

        myMapper.updateByExampleSelective(userProfile, example);
    }

    @Override
    @MethodLog
    public void saveUserProfile(UserProfile userProfile) {
        myMapper.insertSelective(userProfile);
    }
}
