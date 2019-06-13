package com.kangyonggan.blog.service.api;

import com.kangyonggan.blog.dto.AutoReplyRequestDto;

import java.io.InputStream;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
public interface WxService {

    /**
     * 解析请求
     *
     * @param inputStream
     * @return
     */
    AutoReplyRequestDto getRequestDtoFromRequest(InputStream inputStream);

    /**
     * 组装响应报文
     *
     * @param requestDto
     * @return
     */
    String getResponseXml(AutoReplyRequestDto requestDto);

}
