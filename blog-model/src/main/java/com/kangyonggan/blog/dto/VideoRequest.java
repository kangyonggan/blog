package com.kangyonggan.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@ApiModel(description = "视频列表查询请求")
@Data
public class VideoRequest extends PageRequest {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private String[] createdTime;

}
