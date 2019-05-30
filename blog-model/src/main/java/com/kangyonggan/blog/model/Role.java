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
@Table(name = "tb_role")
@Data
@ApiModel(description = "角色相关请求")
public class Role implements Serializable {
    /**
     * 角色ID
     */
    @Id
    @Column(name = "role_id")
    @ApiModelProperty(hidden = true)
    private Long roleId;

    /**
     * 角色代码
     */
    @Column(name = "role_code")
    @ApiModelProperty("角色代码")
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    @ApiModelProperty("角色名称")
    private String roleName;

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