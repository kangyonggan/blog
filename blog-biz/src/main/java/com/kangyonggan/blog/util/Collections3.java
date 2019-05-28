package com.kangyonggan.blog.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Collections工具集
 *
 * @author kangyonggan
 * @since 5/4/18
 */
public final class Collections3 {

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private Collections3() {

    }

    /**
     * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
     *
     * @param collection        来源集合.
     * @param keyPropertyName   要提取为Map中的Key值的属性名.
     * @param valuePropertyName 要提取为Map中的Value值的属性名.
     * @return 返回结果Map
     */
    public static Map extractToMap(Collection collection, String keyPropertyName, String valuePropertyName) {
        Map map = new HashMap(collection.size());

        try {
            for (Object obj : collection) {
                map.put(PropertyUtils.getProperty(obj, keyPropertyName),
                        PropertyUtils.getProperty(obj, valuePropertyName));
            }
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }

        return map;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @return 返回提取后的结果
     */
    public static List extractToList(Collection collection, String propertyName) {
        List list = new ArrayList(collection.size());

        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator    分隔符.
     * @return 返回结果字符串
     */
    public static String extractToString(Collection collection, String propertyName, String separator) {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
     *
     * @param collection 来源集合
     * @param separator  分隔符
     * @return 返回结果字符串
     */
    public static String convertToString(Collection collection, String separator) {
        return StringUtils.join(collection, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix。
     *
     * @param collection 来源集合
     * @param prefix     前缀
     * @param postfix    后缀
     * @return 返回结果字符串
     */
    public static String convertToString(Collection collection, String prefix, String postfix) {
        StringBuilder builder = new StringBuilder();
        for (Object o : collection) {
            builder.append(prefix).append(o).append(postfix);
        }
        return builder.toString();
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 来源集合
     * @return 如果集合为空返回true，否则返回false
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断Map是否为空
     *
     * @param map 来源map
     * @return 如果map为空返回true，否则返回false
     */
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection 来源集合
     * @return 如果集合不为空返回true，否则返回false
     */
    public static boolean isNotEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 来源map
     * @return 如果map不为空返回true，否则返回false
     */
    public static boolean isNotEmpty(Map map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     *
     * @param collection 来源集合
     * @param <T>        元素泛型
     * @return 返回首元素
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        return collection.iterator().next();
    }

    /**
     * 获取Collection的最后一个元素 ，如果collection为空返回null.
     *
     * @param collection 来源集合
     * @param <T>        元素泛型
     * @return 返回尾元素
     */
    public static <T> T getLast(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        // 当类型为List时，直接取得最后一个元素 。
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            return list.get(list.size() - 1);
        }

        // 其他类型通过iterator滚动到最后一个元素.
        Iterator<T> iterator = collection.iterator();
        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext()) {
                return current;
            }
        }
    }

    /**
     * 返回a+b的新List.
     *
     * @param a   集合a
     * @param b   集合b
     * @param <T> 元素泛型
     * @return 返回合并后的集合
     */
    public static <T> List<T> union(Collection<T> a, Collection<T> b) {
        List<T> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 返回a-b的新List
     *
     * @param a   集合a
     * @param b   集合b
     * @param <T> 元素泛型
     * @return 返回a-b的新List
     */
    public static <T> List<T> subtract(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<>(a);
        for (T element : b) {
            list.remove(element);
        }

        return list;
    }

    /**
     * 返回a与b的交集的新List.
     *
     * @param a   集合a
     * @param b   集合b
     * @param <T> 元素泛型
     * @return 返回a与b的交集的新List.
     */
    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<>();

        for (T element : a) {
            if (b.contains(element)) {
                list.add(element);
            }
        }
        return list;
    }
}

