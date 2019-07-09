package com.kangyonggan.blog.service.impl.api;

import com.kangyonggan.blog.dto.AutoReplyRequestDto;
import com.kangyonggan.blog.dto.BaZiDto;
import com.kangyonggan.blog.dto.IdNoDto;
import com.kangyonggan.blog.service.api.WxService;
import com.kangyonggan.blog.util.*;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
@Service
@Log4j2
public class WxServiceImpl implements WxService {

    private static final String ENTER_CHAR = "\n";

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
                // 语言消息
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
    public String getResponseXml(AutoReplyRequestDto requestDto) throws Exception {
        if (!"text".equals(requestDto.getMsgType())) {
            return buildTextMsg(requestDto, "我暂时只能看懂文字，更强大的功能正在开发，敬请期待吧！");
        }

        String content = requestDto.getContent().trim();
        String respXml;

        if ("1".equals(content)) {
            respXml = buildTextMsg(requestDto, "回复：NO_身份证号码 进行查询。如回复：NO_340700198606019586");
        } else if (content.startsWith("NO_")) {
            String idNo = content.substring(3);
            idNo = idNo.replaceAll("x", "X");
            boolean isIdNo15 = IdNoUtil.isIdCard15(idNo);
            if (isIdNo15) {
                idNo = IdNoUtil.convert15To18(idNo);
            }

            String respMsg = "身份证号码（18位）：" + idNo + ENTER_CHAR;
            respMsg += "原户籍地：" + IdNoConstants.getArea(idNo.substring(0, 6)) + ENTER_CHAR;
            respMsg += "出生年月：" + IdNoUtil.getYearFromIdCard(idNo) + "年" + IdNoUtil.getMonthFromIdCard(idNo) + "月" + IdNoUtil.getDayFromIdCard(idNo) + "日" + ENTER_CHAR;
            respMsg += "生肖：" + DestinyUtil.getShengXiao(Integer.parseInt(IdNoUtil.getYearFromIdCard(idNo))) + ENTER_CHAR;
            respMsg += "星座：" + DestinyUtil.getXingZuo(Integer.parseInt(IdNoUtil.getMonthFromIdCard(idNo)), Integer.parseInt(IdNoUtil.getDayFromIdCard(idNo))) + ENTER_CHAR;
            respMsg += "性别：" + (IdNoUtil.getSexFromIdCard(idNo) == 0 ? "男" : "女") + ENTER_CHAR;

            respXml = buildTextMsg(requestDto, respMsg);
        } else if ("2".equals(content)) {
            respXml = buildTextMsg(requestDto, "回复：GENO_地区码 进行生成。如回复：GENO_340700");
        } else if (content.startsWith("GENO_")) {
            IdNoDto idNoDto = new IdNoDto();
            idNoDto.setProv(content.substring(5));
            idNoDto.setLen(-1);
            idNoDto.setStartAge(1);
            idNoDto.setEndAge(120);
            idNoDto.setSex(null);
            idNoDto.setSize(20);
            List<String> idNos = IdNoUtil.genIdCard(idNoDto);

            StringBuilder respMsg = new StringBuilder();
            for (String idNo : idNos) {
                respMsg.append(idNo).append(ENTER_CHAR);
            }

            respXml = buildTextMsg(requestDto, respMsg.toString());
        } else if ("3".equals(content)) {
            respXml = buildTextMsg(requestDto, "回复：BIRTH_阴/阳历-年-月-日-时 进行查询。如回复：BIRTH_阳历-1991-12-27-17");
        } else if (content.startsWith("BIRTH_")) {
            content = content.substring(6);
            String isLunar = content.substring(0, 2);
            content = content.substring(3);
            String[] arr = content.split("-");

            BaZiDto baZiDto = new BaZiDto();
            baZiDto.setIsLunar("阴历".equals(isLunar));
            baZiDto.setYear(Integer.parseInt(arr[0]));
            baZiDto.setMonth(Integer.parseInt(arr[1]));
            baZiDto.setDay(Integer.parseInt(arr[2]));
            baZiDto.setHour(Integer.parseInt(arr[3]));

            if (baZiDto.getIsLunar()) {
                // 阴历转阳历
                String date = CalendarUtil.lunarToSolar(LocalDate.of(baZiDto.getYear(), baZiDto.getMonth(), baZiDto.getDay()).format(DateTimeFormatter.BASIC_ISO_DATE));
                baZiDto.setYear(Integer.parseInt(date.substring(0, 4)));
                baZiDto.setMonth(Integer.parseInt(date.substring(4, 6)));
                baZiDto.setDay(Integer.parseInt(date.substring(6, 8)));
            }

            String baZi = DestinyUtil.getEightWord(baZiDto.getYear(), baZiDto.getMonth(), baZiDto.getDay(), baZiDto.getHour());
            String wuXing = DestinyUtil.getWuXing(baZi);
            String riGan = DestinyUtil.getDayColumn(baZiDto.getYear(), baZiDto.getMonth(), baZiDto.getDay()).substring(0, 1);
            String wuXingOfRiGan = DestinyUtil.getTianGanWuXing(riGan);

            String respMsg = "阳历生日：" + baZiDto.getYear() + "年" + baZiDto.getMonth() + "月" + baZiDto.getDay() + "日" + ENTER_CHAR;
            respMsg += "八　　字：" + baZi + ENTER_CHAR;
            respMsg += "五　　行：" + wuXing + ENTER_CHAR;
            respMsg += "生　　肖：" + DestinyUtil.getShengXiao(baZiDto.getYear()) + ENTER_CHAR;
            respMsg += "星　　座：" + DestinyUtil.getXingZuo(baZiDto.getMonth(), baZiDto.getDay()) + ENTER_CHAR;
            respMsg += "五行强弱：" + DestinyUtil.wuxing(wuXing) + ENTER_CHAR;
            respMsg += "喜　　忌：" + DestinyUtil.getYunShi(wuXingOfRiGan, baZiDto.getMonth()) + ENTER_CHAR;

            respXml = buildTextMsg(requestDto, respMsg);
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
        sb.append("回复编号体验以下功能：\n");
        sb.append("0 查看菜单\n");
        sb.append("1 身份证号码查询\n");
        sb.append("2 生成身份证号码\n");
        sb.append("3 八字、五行查询\n");
        return sb.toString();
    }
}
