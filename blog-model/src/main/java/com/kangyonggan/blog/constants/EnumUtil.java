package com.kangyonggan.blog.constants;

import com.kangyonggan.blog.annotation.Enum;

import java.util.*;

/**
 * @author kangyonggan
 * @since 8/12/18
 */
public final class EnumUtil {

    private static EnumUtil INSTANCE;

    private EnumUtil() {
    }

    public static EnumUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (EnumUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EnumUtil();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * 枚举map
     */
    private Map<String, LinkedHashMap<String, Object>> enumMap = new HashMap<>();

    /**
     * 枚举list
     */
    private Map<String, List<Map<String, Object>>> enumListMap = new HashMap<>();

    private void dealClass(String className) {
        try {
            Class clazz = Class.forName(className);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();

            Enum e = (Enum) clazz.getDeclaredAnnotation(Enum.class);
            if (e != null) {
                Object[] objs = clazz.getEnumConstants();
                for (Object obj : objs) {
                    Object codeValue = clazz.getDeclaredMethod("get" + firstToUpperCase(e.code())).invoke(obj);
                    Object nameValue = clazz.getDeclaredMethod("get" + firstToUpperCase(e.name())).invoke(obj);

                    linkedHashMap.put(String.valueOf(codeValue), nameValue);

                    Map<String, Object> map = new HashMap<>(8);
                    map.put("code", codeValue);
                    map.put("name", nameValue);
                    list.add(map);
                }

                String key = e.key();
                if (key.length() == 0) {
                    key = clazz.getSimpleName();
                }
                enumMap.put(key, linkedHashMap);
                enumListMap.put(key, list);
            }

        } catch (Exception e) {
            throw new RuntimeException("在收集枚举信息时，处理异常:" + className, e);
        }
    }

    public LinkedHashMap<String, Object> getEnumMap(String enumKey) {
        LinkedHashMap<String, Object> map = enumMap.get(enumKey);
        if (map == null) {
            dealClass(getClass().getPackage().getName() + "." + enumKey);
            map = enumMap.get(enumKey);
        }

        return map;
    }

    public List<Map<String, Object>> getEnumList(String enumKey) {
        List<Map<String, Object>> list = enumListMap.get(enumKey);
        if (list == null) {
            dealClass(getClass().getPackage().getName() + "." + enumKey);
            list = enumListMap.get(enumKey);
        }

        return list;
    }

    /**
     * 字符串首字母变大写
     *
     * @param str 字符串
     * @return 首字母变为大写之后的字符串
     */
    private String firstToUpperCase(String str) {
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
