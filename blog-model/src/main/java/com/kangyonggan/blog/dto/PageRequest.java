package com.kangyonggan.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-04-19
 */
@ApiModel(description = "分页请求")
@Data
public class PageRequest extends Request {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", example = "1")
    private Integer pageNum = 1;

    /**
     * 分页大小
     */
    @ApiModelProperty(value = "分页大小", example = "10")
    private Integer pageSize = 10;

}
