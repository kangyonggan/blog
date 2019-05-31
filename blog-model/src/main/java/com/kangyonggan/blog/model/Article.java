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
@Table(name = "tb_article")
@Data
@ApiModel(description = "文章相关请求")
public class Article implements Serializable {
    /**
     * 文章ID
     */
    @Id
    @Column(name = "article_id")
    @ApiModelProperty("文章ID")
    private Long articleId;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 摘要
     */
    @ApiModelProperty("摘要")
    private String summary;

    /**
     * 作者
     */
    @Column(name = "user_id")
    @ApiModelProperty(hidden = true)
    private Long userId;

    /**
     * 阅读量
     */
    @Column(name = "view_num")
    @ApiModelProperty(hidden = true)
    private Integer viewNum;

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
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    private static final long serialVersionUID = 1L;
}