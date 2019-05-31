package com.kangyonggan.blog.constants;

import com.kangyonggan.blog.annotation.Enum;
import lombok.Getter;

/**
 * 小说来源枚举
 *
 * @author kangyonggan
 * @since 8/9/18
 */
@Enum(name = "url")
public enum NovelSource {

    /**
     * 笔趣阁1
     */
    NS01("NS01", "笔趣阁1", "https://www.biquga.com/"),

    /**
     * 笔趣阁2
     */
    NS02("NS02", "笔趣阁2", "https://www.biqubao.com/"),

    /**
     * 笔趣阁3
     */
    NS03("NS03", "笔趣阁3", "https://www.qu.la/"),

    /**
     * 800小说网
     */
    NS04("NS04", "800小说网", "http://www.800txt.net/"),

    /**
     * 言情小说阁
     */
    NS05("NS05", "言情小说阁", "http://www.xianqihaotianmi.com/"),

    /**
     * 笔趣阁4
     */
    NS06("NS06", "笔趣阁4", "https://www.yuanzunxs.cc/"),

    /**
     * 63小说
     */
    NS07("NS07", "63小说", "https://www.63xs.com/"),

    /**
     * 2K小说
     */
    NS08("NS08", "2K小说", "https://www.fpzw.com/"),

    /**
     * 小说宝库
     */
    NS09("NS09", "小说宝库", "https://www.govtz.com/"),

    /**
     * 笔趣阁5
     */
    NS10("NS10", "笔趣阁5", "https://www.ibiquge.net/");

    /**
     * 来源代码
     */
    @Getter
    private final String code;

    /**
     * 来源名称
     */
    @Getter
    private final String name;

    /**
     * 来源地址
     */
    @Getter
    private final String url;

    NovelSource(String code, String name, String url) {
        this.code = code;
        this.name = name;
        this.url = url;
    }

    public static String getUrlByCode(String code) {
        for (NovelSource novelSource : NovelSource.values()) {
            if (novelSource.getCode().equals(code)) {
                return novelSource.getUrl();
            }
        }

        return null;
    }
}
