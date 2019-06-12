package com.kangyonggan.blog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证工具类
 *
 * @author kangyonggan
 * @date 6/23/17
 */
public final class IdNoUtil {

    /**
     * 中国公民身份证号码最小长度。
     */
    private static final int CHINA_ID_MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度。
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 每位加权因子
     */
    private static final int[] POWER = {
            7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
    };

    /**
     * 第18位校检码
     */
    private static final String[] VERIFY_CODE = {
            "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"
    };

    private static Map<String, String> cityCodes = new HashMap(128);

    static {
        cityCodes.put("11", "北京");
        cityCodes.put("12", "天津");
        cityCodes.put("13", "河北");
        cityCodes.put("14", "山西");
        cityCodes.put("15", "内蒙古");
        cityCodes.put("21", "辽宁");
        cityCodes.put("22", "吉林");
        cityCodes.put("23", "黑龙江");
        cityCodes.put("31", "上海");
        cityCodes.put("32", "江苏");
        cityCodes.put("33", "浙江");
        cityCodes.put("34", "安徽");
        cityCodes.put("35", "福建");
        cityCodes.put("36", "江西");
        cityCodes.put("37", "山东");
        cityCodes.put("41", "河南");
        cityCodes.put("42", "湖北");
        cityCodes.put("43", "湖南");
        cityCodes.put("44", "广东");
        cityCodes.put("45", "广西");
        cityCodes.put("46", "海南");
        cityCodes.put("50", "重庆");
        cityCodes.put("51", "四川");
        cityCodes.put("52", "贵州");
        cityCodes.put("53", "云南");
        cityCodes.put("54", "西藏");
        cityCodes.put("61", "陕西");
        cityCodes.put("62", "甘肃");
        cityCodes.put("63", "青海");
        cityCodes.put("64", "宁夏");
        cityCodes.put("65", "新疆");
        cityCodes.put("71", "台湾");
        cityCodes.put("81", "香港");
        cityCodes.put("82", "澳门");
        cityCodes.put("91", "国外");
    }

    private IdNoUtil() {
    }

    /**
     * 15位转18位
     *
     * @param idCard
     * @return
     */
    public static String convert15To18(String idCard) {
        String idCard17 = String.format("%s19%s", idCard.substring(0, 6), idCard.substring(6, 15));

        int iSum = getPowerSum(idCard17);
        String checkCode = getCheckCode18(iSum);

        return String.format("%s%s", idCard17, checkCode);
    }

    /**
     * 18位转15位
     *
     * @param idCard
     * @return
     */
    public static String convert18To15(String idCard) {
        return String.format("%s%s", idCard.substring(0, 6), idCard.substring(8, 17));
    }

    /**
     * 判断是否是身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
        return isIdCard15(idCard) || isIdCard18(idCard);
    }

    /**
     * 判断是否是15位身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard15(String idCard) {
        if (idCard == null || idCard.length() != CHINA_ID_MIN_LENGTH) {
            return false;
        }

        if (!isNumber(idCard)) {
            return false;
        }

        String proCode = idCard.substring(0, 2);
        if (cityCodes.get(proCode) == null) {
            return false;
        }

        try {
            new SimpleDateFormat("yyyyMMdd").parse("19" + idCard.substring(6, 12));
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    /**
     * 判断是否是数字
     *
     * @param idCard
     * @return
     */
    private static boolean isNumber(String idCard) {
        if (idCard == null || idCard.trim().length() == 0) {
            return false;
        }

        return idCard.matches("^[0-9]+$");
    }

    /**
     * 判断是否是数字,包括X
     *
     * @param idCard
     * @return
     */
    private static boolean isNumberWithX(String idCard) {
        if (idCard == null || idCard.trim().length() == 0) {
            return false;
        }

        return idCard.matches("^[0-9X]+$");
    }

    /**
     * 判断是否是18位身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard18(String idCard) {
        if (idCard == null || idCard.length() != CHINA_ID_MAX_LENGTH) {
            return false;
        }

        if (!isNumberWithX(idCard)) {
            return false;
        }

        String proCode = idCard.substring(0, 2);
        if (cityCodes.get(proCode) == null) {
            return false;
        }

        try {
            new SimpleDateFormat("yyyyMMdd").parse(idCard.substring(6, 14));
        } catch (ParseException e) {
            return false;
        }

        int iSum = getPowerSum(idCard);
        String checkCode = getCheckCode18(iSum);
        if (!checkCode.equals(idCard.substring(17, 18))) {
            return false;
        }

        return true;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param idCard
     * @return
     */
    private static int getPowerSum(String idCard) {
        int[] iArr = convertCharToInt(idCard.toCharArray());
        int iSum = 0;
        for (int i = 0; i < POWER.length; i++) {
            iSum = iSum + iArr[i] * POWER[i];
        }
        return iSum;
    }

    /**
     * 将字符数组转换成数字数组
     *
     * @param ch 字符数组
     * @return 数字数组
     */
    private static int[] convertCharToInt(char[] ch) {
        int len = ch.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ch[i]));
            }
        } catch (NumberFormatException e) {
            return iArr;
        }
        return iArr;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param iSum
     * @return 校验位
     */
    private static String getCheckCode18(int iSum) {
        return VERIFY_CODE[iSum % 11];
    }

    /**
     * 根据身份号获取出生日期年
     *
     * @param idCard 身份证号
     * @return 年(yyyy)
     */
    public static String getYearFromIdCard(String idCard) {
        return 18 == idCard.length() ? idCard.substring(6, 10) : "19" + idCard.substring(6, 8);
    }

    /**
     * 根据身份号获取出生日期月
     *
     * @param idCard 身份证号
     * @return 月(MM)
     */
    public static String getMonthFromIdCard(String idCard) {
        return 18 == idCard.length() ? idCard.substring(10, 12) : idCard.substring(8, 10);
    }

    /**
     * 根据身份号获取出生日期日
     *
     * @param idCard 身份证号
     * @return 日(dd)
     */
    public static String getDayFromIdCard(String idCard) {
        return 18 == idCard.length() ? idCard.substring(12, 14) : idCard.substring(10, 12);
    }

    /**
     * 根据身份号获取性别
     *
     * @param idCard 身份证号
     * @return 性别{0:男, 1:女}
     */
    public static int getSexFromIdCard(String idCard) {
        return ((18 == idCard.length() ? idCard.charAt(16) : idCard.charAt(14)) + 1) % 2;
    }
}

