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
@Table(name = "tb_dict")
@Data
@ApiModel(description = "字典相关请求")
public class Dict implements Serializable {
    /**
     * 字典ID
     */
    @Id
    @Column(name = "dict_id")
    @ApiModelProperty("字典ID")
    private Long dictId;

    /**
     * 字典类型
     */
    @Column(name = "dict_type")
    @ApiModelProperty("字典类型")
    private String dictType;

    /**
     * 字典代码
     */
    @Column(name = "dict_code")
    @ApiModelProperty("字典代码")
    private String dictCode;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 排序（从0开始）
     */
    @ApiModelProperty("排序（从0开始）")
    private Integer sort;

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