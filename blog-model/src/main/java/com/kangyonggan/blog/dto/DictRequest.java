package com.kangyonggan.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@ApiModel(description = "字典列表查询请求")
@Data
public class DictRequest extends PageRequest {

    /**
     * 字典类型
     */
    @ApiModelProperty("字典类型")
    private String dictType;

    /**
     * 字典代码
     */
    @ApiModelProperty("字典代码")
    private String dictCode;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private String[] createdTime;

}
