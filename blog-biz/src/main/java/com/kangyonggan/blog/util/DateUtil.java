package com.kangyonggan.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
public final class DateUtil {

    private DateUtil() {
    }

    public static String toXmlDateTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date) + "T" + new SimpleDateFormat("HH:mm:ss.SSS").format(date) + "Z";
    }

}
