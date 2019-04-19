package com.kangyonggan.blog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author longjie
 * @since
 */
public final class StringUtil {

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 下划线
     */
    public static final String UNDER_LINE = "_";

    /**
     * 正则表达式：手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-3,5-9]))\\d{8}$";

    /**
     * 正则表达式：密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{8,20}$";

    /**
     * 正则表达式：邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则匹配：单词
     */
    public static final Pattern PATTERN_WORD = Pattern.compile("[A-Z]([a-z\\d]+)?");

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private StringUtil() {

    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return 如果字符串为空返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return 如果字符串不为空返回true，否则返回false
     */
    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() > 0;
    }

    /**
     * 判断是否有空字符串
     *
     * @param arr 字符串数组
     * @return 如果字符串数组中有空串返回true，否则返回false
     */
    public static boolean hasEmpty(String... arr) {
        for (String str : arr) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否有不为空的字符串
     *
     * @param arr 字符串数组
     * @return 如果字符串数组中有不为空的字符串返回true，否则返回false
     */
    public static boolean hasNotEmpty(String... arr) {
        for (String str : arr) {
            if (isNotEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否在数组中
     *
     * @param str 字符串
     * @param arr 数组
     * @return 如果字符串在数组中返回true，否则返回false
     */
    public static boolean isInArray(String str, String... arr) {
        for (String s : arr) {
            if (str.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰字符串转数组
     *
     * @param str 字符串
     * @return 转换后的数组
     */
    public static String[] camelToArray(String str) {
        StringBuilder sb = new StringBuilder();

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (Character.isUpperCase(ch)) {
                sb.append(UNDER_LINE).append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }

        if (sb.toString().startsWith(UNDER_LINE)) {
            sb.deleteCharAt(0);
        }
        if (sb.toString().endsWith(UNDER_LINE)) {
            sb.deleteCharAt(sb.lastIndexOf(UNDER_LINE));
        }

        return sb.toString().split(UNDER_LINE);
    }

    /**
     * 字符串首字母变大写
     *
     * @param str 字符串
     * @return 首字母变为大写之后的字符串
     */
    public static String firstToUpperCase(String str) {
        if (isEmpty(str)) {
            return EMPTY;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 字符串两边都加上%
     *
     * @param str 字符串
     * @return 两边都加上%后的字符串
     */
    public static String toLike(String str) {
        if (isEmpty(str)) {
            str = EMPTY;
        }
        return String.format("%%%s%%", str);
    }

    /**
     * 驼峰字符串转下划线字符串
     *
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public static String camelToUnderLine(String str) {
        if (isEmpty(str)) {
            return EMPTY;
        }

        str = String.valueOf(str.charAt(0)).toUpperCase().concat(str.substring(1));

        StringBuilder sb = new StringBuilder();
        Matcher matcher = PATTERN_WORD.matcher(str);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(UNDER_LINE);
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    /**
     * 下划线字符串转驼峰字符串
     *
     * @param str 下划线字符串
     * @return 驼峰字符串
     */
    public static String underLineToCamel(String str) {
        if (isEmpty(str)) {
            return EMPTY;
        }

        String[] arr = str.split(UNDER_LINE);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                sb.append(arr[i]);
            } else {
                sb.append(firstToUpperCase(arr[i]));
            }
        }

        return sb.toString();
    }

    /**
     * 判断字符串是不是手机号
     *
     * @param str 字符串
     * @return 是手机号返回true，否则返回false
     */
    public static boolean isMobile(String str) {
        return Pattern.matches(REGEX_MOBILE, str);
    }

    /**
     * 判断字符串是不是密码
     *
     * @param str 字符串
     * @return 是密码返回true，否则返回false
     */
    public static boolean isPassword(String str) {
        return Pattern.matches(REGEX_PASSWORD, str);
    }

    /**
     * 判断字符串是不是邮箱
     *
     * @param str 字符串
     * @return 是邮箱返回true，否则返回false
     */
    public static boolean isEmail(String str) {
        return Pattern.matches(REGEX_EMAIL, str);
    }

    /**
     * 判断字符串是不是汉字
     *
     * @param str 字符串
     * @return 是汉字返回true，否则返回false
     */
    public static boolean isChinese(String str) {
        return Pattern.matches(REGEX_CHINESE, str);
    }

    /**
     * 判断字符串是不是URL
     *
     * @param str 字符串
     * @return 是URL返回true，否则返回false
     */
    public static boolean isUrl(String str) {
        return Pattern.matches(REGEX_URL, str);
    }

}
