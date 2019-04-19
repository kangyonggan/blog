package com.kangyonggan.blog.dto;

import com.kangyonggan.blog.model.UserProfile;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-04-02
 */
@Data
public class UserDto extends UserProfile {

    /**
     * 电子邮箱
     */
    private String email;

}
