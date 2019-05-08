package com.kangyonggan.blog.dto;

import com.kangyonggan.blog.model.Novel;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019/5/8 0008
 */
@Data
public class NovelDto extends Novel {

    /**
     * 最新章节ID
     */
    private Long lastSectionId;

    /**
     * 最新章节名称
     */
    private String lastSectionTitle;

}
