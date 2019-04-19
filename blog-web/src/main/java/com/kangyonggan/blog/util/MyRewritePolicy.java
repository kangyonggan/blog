package com.kangyonggan.blog.util;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author kangyonggan
 * @since 2019/4/14 0014
 */
@Plugin(name = "MyRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public class MyRewritePolicy implements RewritePolicy {

    private static final int MOBILE_LEN = 11;

    /**
     * 手机号码前三位,^((13\\d)|(14[5|7])|(15([^4]))|(18\\d)|(17[0|6|7]))
     */
    private final static String[] MOBILE_START_NO = {
            "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
            "145", "147",
            "150", "151", "152", "153", "155", "156", "157", "158", "159",
            "170", "176", "177",
            "180", "181", "182", "183", "184", "185", "186", "187", "188", "189"};
    /**
     * 省、直辖市代码表：
     * 11 : 北京  12 : 天津  13 : 河北       14 : 山西  15 : 内蒙古
     * 21 : 辽宁  22 : 吉林  23 : 黑龙江  	31 : 上海  32 : 江苏
     * 33 : 浙江  34 : 安徽  35 : 福建       36 : 江西  37 : 山东
     * 41 : 河南  42 : 湖北  43 : 湖南       44 : 广东  45 : 广西      46 : 海南
     * 50 : 重庆  51 : 四川  52 : 贵州       53 : 云南  54 : 西藏
     * 61 : 陕西  62 : 甘肃  63 : 青海       64 : 宁夏  65 : 新疆
     * 71 : 台湾
     * 81 : 香港  82 : 澳门
     * 91 : 国外
     */
    private final String[] PROVINCE_CODE = {"11", "12", "13", "14", "15", "21",
            "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42",
            "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
            "63", "64", "65", "71", "81", "82", "91"};

    private final static SimpleDateFormat SDF8 = new SimpleDateFormat("yyyyMMdd");

    private final static String HIDDEN_STR = "****";

    private boolean debug;

    public MyRewritePolicy(boolean debug) {
        this.debug = debug;
    }

    @Override
    public LogEvent rewrite(LogEvent event) {
        if (debug) {
            return event;
        }

        Message msg = event.getMessage();
        if (msg == null) {
            return event;
        }
        String message = msg.getFormattedMessage();

        // 处理日志的逻辑
        if (null != message) {
            message = processData(message);
        }
        SimpleMessage simpleMessage = new SimpleMessage(message);
        return new Log4jLogEvent.Builder(event).setMessage(simpleMessage).build();
    }

    /**
     * 敏感信息处理：手机号、身份证号处理。只需传入要处理的字符串，自动处理满足手机号或身份证格式的数字
     *
     * @param str
     * @return
     */
    private String processData(String str) {
        //字符串长度
        int strLen = str.length();
        if (strLen < MOBILE_LEN) {
            return str;
        }

        //匹配字符串
        StringBuilder sb = new StringBuilder();
        //清空缓存数据
        sb.setLength(0);
        //匹配字符串长度
        int matchLen = 0;
        for (int charIndex = 0; charIndex < strLen; charIndex++) {
            //当前字符
            char currentChar = str.charAt(charIndex);
            //第一位数字校验:非零数字
            if (0 == matchLen) {
                if (isFirstNum(currentChar)) {
                    sb.append(currentChar);
                    matchLen++;
                }
                continue;
            } else {
                if (Character.isDigit(currentChar)) {
                    sb.append(currentChar);
                    matchLen++;
                } else {
                    if (matchLen == 11 || matchLen == 15 || matchLen == 18) {
                        str = checkAndReplaceStr(str, sb.toString(), matchLen);
                    } else if (matchLen == 17 && "X".equalsIgnoreCase(currentChar + "")) {
                        sb.append(currentChar);
                        matchLen++;
                        str = checkAndReplaceStr(str, sb.toString(), matchLen);
                    }
                    //清空缓存数据
                    sb.setLength(0);
                    matchLen = 0;
                    continue;
                }
            }
            boolean temp = matchLen == 11 || matchLen == 15 || matchLen == 18;
            if (charIndex == (strLen - 1) && temp) {
                str = checkAndReplaceStr(str, sb.toString(), matchLen);
            }
        }
        return str;
    }

    /**
     * 校验数字并替换
     *
     * @param oldStr
     * @param matchStr
     * @param length
     */
    private String checkAndReplaceStr(String oldStr, String matchStr, int length) {
        if (length == MOBILE_LEN) {
            //手机号
            if (Arrays.binarySearch(MOBILE_START_NO, matchStr.substring(0, MOBILE_START_NO[0].length())) > -1) {
                oldStr = oldStr.replaceAll(matchStr, matchStr.substring(0, 3) + HIDDEN_STR + matchStr.substring(7));
            }
        } else {
            //身份证号
            // 校验省份
            if (Arrays.binarySearch(PROVINCE_CODE, matchStr.substring(0, PROVINCE_CODE[0].length())) > -1) {
                try {
                    //当前日期
                    Date today = new Date();
                    //身份证最早年月日：19000101
                    Date beginDate = SDF8.parse("19000101");
                    SDF8.setLenient(false);
                    //字符串匹配的年月日
                    String yearDate;
                    Date matchDate;
                    int idNoLen = 15;
                    if (length == idNoLen) {
                        yearDate = "19" + matchStr.substring(6, 12);
                        matchDate = SDF8.parse(yearDate);
                        if (matchDate.after(beginDate) && matchDate.before(today)) {
                            oldStr = oldStr.replace(matchStr, matchStr.substring(0, matchStr.length() - 7) + HIDDEN_STR + matchStr.substring(matchStr.length() - 3));
                        }
                    } else {
                        yearDate = matchStr.substring(6, 14);
                        matchDate = SDF8.parse(yearDate);
                        if (matchDate.after(beginDate) && matchDate.before(today)) {
                            oldStr = oldStr.replace(matchStr, matchStr.substring(0, matchStr.length() - 8) + HIDDEN_STR + matchStr.substring(matchStr.length() - 4));
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return oldStr;
    }

    /**
     * 判断是否非0数字
     *
     * @param c
     * @return
     */
    private boolean isFirstNum(char c) {
        return (Character.isDigit(c) && !('0' == c));
    }

    @PluginFactory
    public static MyRewritePolicy factory(@PluginAttribute("debug") String debug) {
        return new MyRewritePolicy(Booleans.parseBoolean(debug, true));
    }

}
