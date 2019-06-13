package com.kangyonggan.blog.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
public final class XmlUtil {

    private XmlUtil() {
    }

    private static final SAXReader reader = new SAXReader();

    /**
     * xml格式化
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String format(String data) throws Exception {
        SAXReader reader = new SAXReader();
        StringReader in = new StringReader(data);

        XMLWriter writer = null;
        try {
            Document doc = reader.read(in);

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            format.setIndentSize(4);
            format.setExpandEmptyElements(true);
            StringWriter out = new StringWriter();
            writer = new XMLWriter(out, format);

            writer.write(doc);
            writer.flush();

            return out.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static Document read(String filename) throws DocumentException {
        return reader.read(new File(filename));
    }

    public static Document read(File file) throws DocumentException {
        return reader.read(file);
    }

    public static Document read(URL url) throws DocumentException {
        return reader.read(url);
    }

    public static Document parseText(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }
}
