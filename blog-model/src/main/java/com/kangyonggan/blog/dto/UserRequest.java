package com.kangyonggan.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@ApiModel(description = "用户列表查询请求")
@Data
public class UserRequest extends PageRequest {

    /**
     * 电子邮箱
     */
    @ApiModelProperty("电子邮箱")
    private String email;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private String[] createdTime;

}
