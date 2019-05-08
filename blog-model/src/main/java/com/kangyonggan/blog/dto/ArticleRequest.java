package com.kangyonggan.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-04-19
 */
@ApiModel(description = "文章请求")
@Data
public class ArticleRequest extends PageRequest {

    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字", example = "java")
    private String key;

}
