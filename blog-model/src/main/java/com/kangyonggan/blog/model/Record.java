package com.kangyonggan.blog.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 记录
 *
 * @author kangyonggan
 * @since 8/8/18
 */
@Table(name = "tb_record")
@Data
public class Record implements Serializable {
    /**
     * 记录ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 图片
     */
    @Column(name = "file_names")
    private String fileNames;

    /**
     * 视频
     */
    @Column(name = "video_names")
    private String videoNames;

    /**
     * openid
     */
    private String openid;

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

    /**
     * 内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}