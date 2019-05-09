package com.kangyonggan.blog.model;

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
@Table(name = "tb_video")
@Data
public class Video implements Serializable {
    /**
     * 视频ID
     */
    @Id
    @Column(name = "video_id")
    private Long videoId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 视频封面
     */
    private String cover;

    /**
     * 视频代码
     */
    private String content;

    /**
     * 观看量
     */
    @Column(name = "view_num")
    private Integer viewNum;

    /**
     * 逻辑删除
     */
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}