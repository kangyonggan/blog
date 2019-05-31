package com.kangyonggan.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@ApiModel(description = "小说列表查询请求")
@Data
public class NovelRequest extends PageRequest {

    /**
     * 小说名称
     */
    @ApiModelProperty("小说名称")
    private String name;

    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private String author;

}
