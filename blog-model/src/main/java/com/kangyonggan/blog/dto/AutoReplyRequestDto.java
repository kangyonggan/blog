package com.kangyonggan.blog.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
@Data
public class AutoReplyRequestDto implements Serializable {
    /**
     * 开发者微信号
     */
    private String toUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private String createTime;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 文本消息内容
     */
    private String content;

    /**
     * 图片url
     */
    private String picUrl;

    /**
     * 消息id，64位整型
     */
    private String msgId;
}
