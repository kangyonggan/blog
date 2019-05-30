package com.kangyonggan.blog.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author kangyonggan
 * @since 8/8/18
 */
@Table(name = "tb_menu")
@Data
@ApiModel(description = "菜单相关请求")
public class Menu implements Serializable {
    /**
     * 菜单ID
     */
    @Id
    @Column(name = "menu_id")
    @ApiModelProperty("菜单ID")
    private Long menuId;

    /**
     * 菜单代码
     */
    @Column(name = "menu_code")
    @ApiModelProperty("菜单代码")
    private String menuCode;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    @ApiModelProperty("菜单名称")
    private String menuName;

    /**
     * 父菜单代码
     */
    @Column(name = "parent_code")
    @ApiModelProperty("父菜单代码")
    private String parentCode;

    /**
     * 菜单排序(从0开始)
     */
    @ApiModelProperty("菜单排序(从0开始)")
    private Integer sort;

    /**
     * 菜单图标的样式
     */
    @ApiModelProperty("菜单图标的样式")
    private String icon;

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

    /**
     * 子菜单
     */
    @Transient
    @ApiModelProperty(hidden = true)
    private List<Menu> children;

    private static final long serialVersionUID = 1L;
}