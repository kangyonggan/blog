package com.kangyonggan.blog.mapper;

import com.kangyonggan.blog.MyMapper;
import com.kangyonggan.blog.dto.UserDto;
import com.kangyonggan.blog.model.User;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.provider.SqlServerProvider;

/**
 * @author kangyonggan
 * @since 8/8/18
 */
public interface UserMapper extends MyMapper<User> {

    /**
     * 重新指定主键
     *
     * @param user
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    @InsertProvider(type = SqlServerProvider.class, method = "dynamicSQL")
    @Override
    int insertSelective(User user);

    /**
     * 添加用户角色
     *
     * @param userId
     * @param roleIds
     */
    void insertUserRoles(@Param("userId") Long userId, @Param("roleIds") String[] roleIds);

    /**
     * 查找用户信息
     *
     * @param userId
     * @return
     */
    UserDto selectUserDtoById(Long userId);

}