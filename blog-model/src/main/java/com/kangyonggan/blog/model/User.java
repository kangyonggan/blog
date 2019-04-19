package com.kangyonggan.blog.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author kangyonggan
 * @since 8/8/18
 */
@Table(name = "tb_user")
@Data
@ApiModel(description = "用户")
public class User implements Serializable {
    /**
     * 用户ID
     */
    @Id
    @Column(name = "user_id")
    @ApiModelProperty(hidden = true)
    private Long userId;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件", required = true, example = "admin@kangyonggan.com")
    private String email;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true, example = "11111111")
    private String password;

    /**
     * 密码盐
     */
    @ApiModelProperty(hidden = true)
    private String salt;

    /**
     * 逻辑删除
     */
    @Column(name = "is_deleted")
    @ApiModelProperty(hidden = true)
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @ApiModelProperty(hidden = true)
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    @ApiModelProperty(hidden = true)
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}