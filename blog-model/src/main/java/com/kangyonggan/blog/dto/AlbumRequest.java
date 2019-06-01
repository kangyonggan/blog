package com.kangyonggan.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@ApiModel(description = "相册列表查询请求")
@Data
public class AlbumRequest extends PageRequest {

    /**
     * 相册名称
     */
    @ApiModelProperty("相册名称")
    private String albumName;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private String[] createdTime;

}
