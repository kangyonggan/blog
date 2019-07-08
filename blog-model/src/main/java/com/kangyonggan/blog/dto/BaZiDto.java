package com.kangyonggan.blog.dto;

import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-06-12
 */
@Data
public class BaZiDto extends Request {

    private Boolean isLunar;

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer hour;

}
