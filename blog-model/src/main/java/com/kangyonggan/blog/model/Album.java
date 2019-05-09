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
@Table(name = "tb_album")
@Data
public class Album implements Serializable {
    /**
     * 相册ID
     */
    @Id
    @Column(name = "album_id")
    private Long albumId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 相册名称
     */
    @Column(name = "album_name")
    private String albumName;

    /**
     * 封面
     */
    private String cover;

    /**
     * 密码
     */
    private String password;

    /**
     * 大小
     */
    private Integer size;

    /**
     * 排序(从0开始)
     */
    private Integer sort;

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