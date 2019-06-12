package com.kangyonggan.blog.dto;

import lombok.Data;

/**
 * @author kangyonggan
 * @since 2019-06-12
 */
@Data
public class IdNoDto extends Request {

    private String prov;

    private Integer startAge = 1;

    private Integer endAge = 60;

    private String sex;

    private Integer len = -1;

    private Integer size = 20;

}
