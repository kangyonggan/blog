package com.kangyonggan.blog.util;

import org.pegdown.PegDownProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2019-06-03
 */
public final class MarkdownUtil {

    private MarkdownUtil() {
    }

    /**
     * 生成markdown目录
     *
     * @param content md语法的内容
     * @return
     */
    public static List<Map<String, Object>> genTocs(String content) {
        List<Map<String, Object>> docs = new ArrayList<>();

        String[] lines = content.split("\n");

        List<Map<String, Object>> childrenOne = null;
        List<Map<String, Object>> childrenTwo = null;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // 判断目录级别
            if (line.startsWith("## ")) {
                String name = (docs.size() + 1) + ". " + line.substring(3);
                String link = line.substring(3).replaceAll("\\.", "").toLowerCase();
                childrenOne = new ArrayList<>();

                Map<String, Object> header = new HashMap<>(8);
                header.put("name", name);
                header.put("link", link);
                header.put("children", childrenOne);
                docs.add(header);
            } else if (line.startsWith("### ")) {
                if (childrenOne == null) {
                    continue;
                }
                String name = docs.size() + "." + (childrenOne.size() + 1) + ". " + line.substring(4);
                String link = line.substring(4).replaceAll("\\.", "").toLowerCase();
                childrenTwo = new ArrayList<>();

                Map<String, Object> header = new HashMap<>(8);
                header.put("name", name);
                header.put("link", link);
                header.put("children", childrenTwo);
                childrenOne.add(header);
            } else if (line.startsWith("#### ")) {
                if (childrenTwo == null) {
                    continue;
                }
                String name = docs.size() + "." + childrenOne.size() + "." + (childrenTwo.size() + 1) + ". " + line.substring(5);
                String link = line.substring(5).replaceAll("\\.", "").toLowerCase();

                Map<String, Object> header = new HashMap<>(8);
                header.put("name", name);
                header.put("link", link);
                header.put("children", new ArrayList<>());
                childrenTwo.add(header);
            }

        }

        return docs;
    }

    /**
     * markdown语法转html语法
     *
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        return new PegDownProcessor(Integer.MAX_VALUE).markdownToHtml(markdown);
    }
}
