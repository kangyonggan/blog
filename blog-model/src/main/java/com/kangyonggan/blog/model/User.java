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
@ApiModel(description = "用户相关请求")
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
    @ApiModelProperty("电子邮件")
    private String email;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 密码盐
     */
    @ApiModelProperty(hidden = true)
    private String salt;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "updated_time")
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}