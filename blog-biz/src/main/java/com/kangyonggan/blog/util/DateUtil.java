package com.kangyonggan.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
public final class DateUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");

    private DateUtil() {}

    public static String toXmlDateTime(Date date) {
        return DATE_FORMAT.format(date) + "T" + TIME_FORMAT.format(date) + "Z";
    }

}
