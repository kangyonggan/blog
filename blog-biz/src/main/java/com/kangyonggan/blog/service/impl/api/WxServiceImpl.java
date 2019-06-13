package com.kangyonggan.blog.service.impl.api;

import com.kangyonggan.blog.dto.AutoReplyRequestDto;
import com.kangyonggan.blog.service.api.WxService;
import com.kangyonggan.blog.util.IoUtil;
import com.kangyonggan.blog.util.XmlUtil;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
@Service
@Log4j2
public class WxServiceImpl implements WxService {

    /**
     * 文本信息模板
     */
    private static String TEXT_XML_TEMPLATE = "<xml>" +
            "<ToUserName><![CDATA[%s]]></ToUserName>" +
            "<FromUserName><![CDATA[%s]]></FromUserName>" +
            "<CreateTime>%d</CreateTime>" +
            "<MsgType><![CDATA[text]]></MsgType>" +
            "<Content><![CDATA[%s]]></Content>" +
            "</xml>";

    /**
     * 图文信息模板
     */
    private static String NEWS_XML_TEMPLATE = "<xml>\n" +
            "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
            "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
            "<CreateTime>%d</CreateTime>\n" +
            "<MsgType><![CDATA[news]]></MsgType>\n" +
            "<ArticleCount>1</ArticleCount>\n" +
            "<Articles>\n" +
            "<item>\n" +
            "<Title><![CDATA[%s]]></Title> \n" +
            "<Description><![CDATA[%s]]></Description>\n" +
            "<PicUrl><![CDATA[%s]]></PicUrl>\n" +
            "<Url><![CDATA[%s]]></Url>\n" +
            "</item>\n" +
            "</Articles>\n" +
            "</xml>";

    /**
     * 解析请求
     *
     * @param inputStream
     * @return
     */
    @Override
    public AutoReplyRequestDto getRequestDtoFromRequest(InputStream inputStream) {
        AutoReplyRequestDto requestDto = new AutoReplyRequestDto();

        // 获取请求报文
        String xml;
        try {
            xml = IoUtil.read(inputStream);
            log.info("接收到的xml：{}", xml);
        } catch (IOException e) {
            log.warn("读取输入流异常", e);
            return null;
        }

        Document doc;
        try {
            doc = XmlUtil.parseText(xml);
            log.info("xml解析成功");
        } catch (DocumentException e) {
            log.warn("xml解析异常", e);
            return null;
        }

        try {
            Element root = doc.getRootElement();
            // 开发者微信号
            String toUserName = root.element("ToUserName").getTextTrim();
            // 发送方帐号（一个OpenID）
            String fromUserName = root.element("FromUserName").getTextTrim();
            // 消息创建时间 （整型）
            String createTime = root.element("CreateTime").getTextTrim();
            // text
            String msgType = root.element("MsgType").getTextTrim();
            if ("text".equals(msgType)) {
                // 文本消息内容
                String content = root.element("Content").getTextTrim();
                requestDto.setContent(content);
            } else if ("image".equals(msgType)) {
                // 图片url
                String picUrl = root.element("PicUrl").getTextTrim();
                requestDto.setPicUrl(picUrl);
            } else if ("voice".equals(msgType)) {
                // TODO 语言消息
            }
            // 消息id，64位整型
            String msgId = root.element("MsgId").getTextTrim();

            requestDto.setMsgId(msgId);
            requestDto.setToUserName(toUserName);
            requestDto.setMsgType(msgType);
            requestDto.setFromUserName(fromUserName);
            requestDto.setCreateTime(createTime);
        } catch (Exception e) {
            log.warn("提取requestDto异常", e);
            return null;
        }

        return requestDto;
    }

    /**
     * 组装响应报文
     *
     * @param requestDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String getResponseXml(AutoReplyRequestDto requestDto) {
        if (!"text".equals(requestDto.getMsgType())) {
            return buildTextMsg(requestDto, "我暂时只能看懂文字，更强大的功能正在开发，敬请期待吧！");
        }

        String content = requestDto.getContent();
        String respXml;

        if (content.contains("瓜皮")) {
            respXml = buildTextMsg(requestDto, "湿垃圾");
        } else {
            // 菜单
            respXml = buildTextMsg(requestDto, getMenus());
        }

        return respXml;
    }

    private String buildTextMsg(AutoReplyRequestDto requestDto, String content) {
        return String.format(TEXT_XML_TEMPLATE, requestDto.getFromUserName(), requestDto.getToUserName(), System.currentTimeMillis(), content);
    }

    private String getMenus() {
        StringBuilder sb = new StringBuilder();
        sb.append("垃圾分分类，资源不浪费。\n");
        sb.append("垃圾也有宝，分类不可少。\n");
        sb.append("垃圾不分类，等于在浪费。\n");
        sb.append("混放是垃圾，分类成资源。\n");
        sb.append("分类一小步，文明一大步。\n\n\n");
        sb.append("回复垃圾名称，获取垃圾分类\n如回复：西瓜皮，会告诉你是湿垃圾\n");
        return sb.toString();
    }
}
