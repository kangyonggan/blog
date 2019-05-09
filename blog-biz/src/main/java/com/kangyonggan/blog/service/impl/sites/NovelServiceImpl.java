package com.kangyonggan.blog.service.impl.sites;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.NovelSource;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.dto.NovelDto;
import com.kangyonggan.blog.mapper.NovelMapper;
import com.kangyonggan.blog.model.Novel;
import com.kangyonggan.blog.model.NovelQueue;
import com.kangyonggan.blog.model.Section;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.NovelQueueService;
import com.kangyonggan.blog.service.sites.NovelService;
import com.kangyonggan.blog.service.sites.SectionService;
import com.kangyonggan.blog.util.HtmlUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kangyonggan
 * @since 2019-05-08
 */
@Service
@Log4j2
public class NovelServiceImpl extends BaseService<Novel> implements NovelService {

    /**
     * 线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * 线程是否启动
     */
    private boolean isStarting;

    @Autowired
    private NovelQueueService novelQueueService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private NovelMapper novelMapper;

    @Override
    @MethodLog
    public void pullNovels(String novelIds) {
        if (StringUtils.isEmpty(novelIds)) {
            return;
        }

        String[] ids = novelIds.split(",");
        for (String id : ids) {
            if (!novelQueueService.exists(Long.parseLong(id))) {
                novelQueueService.saveNovelQueue(Long.parseLong(id));
            }
        }
        log.info("小说已经放入队列:{}", novelIds);
        popOrCheck(true);
    }

    @Override
    @MethodLog
    public List<NovelDto> searchNovels(String key) {
        return novelMapper.searchNovels(key);
    }

    @Override
    public List<Novel> findNewNovels() {
        Example example = new Example(Novel.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        example.selectProperties("novelId", "name", "author", "summary", "cover", "updatedTime");
        example.orderBy("novelId").desc();
        PageHelper.startPage(1, 6);

        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public Novel findNovelById(Long novelId) {
        return myMapper.selectByPrimaryKey(novelId);
    }

    /**
     * 如果没有线程在消费队列，则启动一个
     *
     * @param isCheck
     * @return
     */
    private synchronized Long popOrCheck(boolean isCheck) {
        if (isCheck) {
            if (!isStarting) {
                startThread();
            }
            return null;
        } else {
            NovelQueue novelQueue = novelQueueService.findNextNovel();
            if (novelQueue == null) {
                isStarting = false;
                log.info("队列中没有待更新的小说了，线程启动标识置为false");
                return null;
            }

            return novelQueue.getNovelId();
        }
    }

    /**
     * 启动消费线程
     */
    private void startThread() {
        isStarting = true;
        executorService.execute(() -> {
            while (true) {
                Long id = null;
                try {
                    id = popOrCheck(false);

                    if (id == null) {
                        log.info("队列中没有待更新的小说了，关闭线程");
                        break;
                    }

                    // 更新小说
                    pullNovel(id);
                    log.info("小说{}更新完成", id);
                } catch (Exception e) {
                    log.error("消费线程出现异常, 继续处理下一个", e);
                } finally {
                    if (id != null) {
                        novelQueueService.finished(id);
                    }
                }
            }
        });

        log.info("队列消费线程启动成功");
    }

    /**
     * 更新小说
     *
     * @param novelId
     */
    private void pullNovel(Long novelId) {
        Novel novel = myMapper.selectByPrimaryKey(novelId);
        if (novel == null) {
            log.info("小说{}不存在，无法更新", novelId);
            return;
        }

        // 查找最新章节
        Section lastSection = sectionService.findLastSection(novel.getNovelId());

        // 获取章节列表
        Elements sectionList = getSectionList(novel);
        if (sectionList == null) {
            return;
        }

        // 获取开始下标
        int startIndex = getStartIndex(novel);

        // 定位到最新章节
        if (lastSection != null) {
            for (int i = startIndex; i < sectionList.size(); i++) {
                Element element = sectionList.get(i);
                String code = element.attr("href");
                code = code.substring(code.lastIndexOf("/") + 1, code.lastIndexOf("."));
                if (lastSection.getCode().equals(code)) {
                    startIndex = i + 1;
                    break;
                }
            }
        }

        // 从最新章节更新到最后
        for (int i = startIndex; i < sectionList.size(); i++) {
            Element element = sectionList.get(i);
            String code = element.attr("href");
            code = code.substring(code.lastIndexOf("/") + 1, code.lastIndexOf("."));
            // 解析章节
            parseSection(novel, code);
        }
    }

    /**
     * 解析章节
     *
     * @param novel
     * @param code
     */
    private void parseSection(Novel novel, String code) {
        Section section = getSection(novel, code);

        sectionService.saveSection(section);
        log.info("章节【{}】保存成功", section.getTitle());
    }

    /**
     * 获取章节
     *
     * @param novel
     * @param code
     * @return
     */
    private Section getSection(Novel novel, String code) {
        Section section = new Section();
        String title = "";
        String content = "";
        if (NovelSource.NS01.getCode().equals(novel.getSource())) {
            // biquga
            Document sectionDoc = HtmlUtil.parseUrl(NovelSource.NS01.getUrl() + novel.getCode() + "/" + code + ".html");
            title = sectionDoc.select(".bookname h1").html().trim();
            content = sectionDoc.select("#content").html();
        } else if (NovelSource.NS02.getCode().equals(novel.getSource())) {
            // biqubao
            Document sectionDoc = HtmlUtil.parseUrl(NovelSource.NS02.getUrl() + "book/" + novel.getCode() + "/" + code + ".html");
            title = sectionDoc.select(".bookname h1").html().trim();
            content = sectionDoc.select("#content").html();
        } else if (NovelSource.NS03.getCode().equals(novel.getSource())) {
            // qu.la
            Document sectionDoc = HtmlUtil.parseUrl(NovelSource.NS03.getUrl() + "book/" + novel.getCode() + "/" + code + ".html");
            title = sectionDoc.select(".bookname h1").html().trim();
            content = sectionDoc.select("#content").html();
        } else if (NovelSource.NS04.getCode().equals(novel.getSource())) {
            // 800txt
            Document sectionDoc = HtmlUtil.parseUrl(NovelSource.NS04.getUrl() + "book_" + novel.getCode() + "/" + code + ".html");
            title = sectionDoc.select(".bookname h1").html().trim();
            content = sectionDoc.select("#content").html();
        } else if (NovelSource.NS05.getCode().equals(novel.getSource())) {
            // xianqihaotianmi
            Document sectionDoc = HtmlUtil.parseUrl(NovelSource.NS05.getUrl() + "read/" + code + ".html");
            title = sectionDoc.select(".panel-heading").html().replaceAll("全部章节", "").trim();
            content = sectionDoc.select(".content-body").html();
        } else if (NovelSource.NS06.getCode().equals(novel.getSource())) {
            // yuanzunxs
            Document sectionDoc = HtmlUtil.parseUrl(NovelSource.NS06.getUrl() + "go/" + novel.getCode() + "/" + code + ".html");
            title = sectionDoc.select("h1.pt10").html().trim().replaceAll("<small>.*</small>", "");
            content = sectionDoc.select(".readcontent").html();
        } else if (NovelSource.NS07.getCode().equals(novel.getSource())) {
            // 63xs
            Document sectionDoc = HtmlUtil.parseUrl(NovelSource.NS07.getUrl() + "book/" + novel.getCode() + "/" + code + ".html");
            title = sectionDoc.select(".bookname h1").html().trim().replace("章节目录 ", "");
            content = sectionDoc.select("#content").html();
        } else if (NovelSource.NS08.getCode().equals(novel.getSource())) {
            // 2K小说
            Document sectionDoc = HtmlUtil.parseUrl(NovelSource.NS08.getUrl() + "xiaoshuo/" + novel.getCode() + "/" + code + ".html");
            title = sectionDoc.select("#box h2").html().trim();
            content = sectionDoc.select("#box p.Text").html();
        } else {
            log.error("未知小说源, name={}, source={}", novel.getName(), novel.getSource());
        }

        // 内容过滤
        content = filterContent(content);

        section.setNovelId(novel.getNovelId());
        section.setCode(code);
        section.setTitle(title);
        section.setContent(content);

        return section;
    }

    /**
     * 内容过滤
     *
     * @param content
     * @return
     */
    private String filterContent(String content) {
        content = content.replaceAll("……", "...");
        content = content.replaceAll("———", "");
        return content;
    }

    /**
     * 获取开始下标
     *
     * @param novel
     * @return
     */
    private int getStartIndex(Novel novel) {
        int startIndex = 0;
        if (NovelSource.NS01.getCode().equals(novel.getSource())) {
            // biquga
            startIndex = 9;
        } else if (NovelSource.NS02.getCode().equals(novel.getSource())) {
            // biqubao
        } else if (NovelSource.NS03.getCode().equals(novel.getSource())) {
            // qu.la
            startIndex = 12;
        } else if (NovelSource.NS04.getCode().equals(novel.getSource())) {
            // 800txt
        } else if (NovelSource.NS05.getCode().equals(novel.getSource())) {
            // xianqihaotianmi
        } else if (NovelSource.NS06.getCode().equals(novel.getSource())) {
            // yuanzunxs
        } else if (NovelSource.NS07.getCode().equals(novel.getSource())) {
            // 63xs
        } else if (NovelSource.NS08.getCode().equals(novel.getSource())) {
            // 2K小说
            startIndex = 4;
        } else {
            log.error("未知小说源, name={}, source={}", novel.getName(), novel.getSource());
        }
        return startIndex;
    }

    /**
     * 获取章节列表
     *
     * @param novel
     * @return
     */
    private Elements getSectionList(Novel novel) {
        if (NovelSource.NS01.getCode().equals(novel.getSource())) {
            // biquga
            Document document = HtmlUtil.parseUrl(NovelSource.NS01.getUrl() + novel.getCode());
            return document.select("#list dd a");
        } else if (NovelSource.NS02.getCode().equals(novel.getSource())) {
            // biqubao
            Document document = HtmlUtil.parseUrl(NovelSource.NS02.getUrl() + "book/" + novel.getCode());
            return document.select("#list dd a");
        } else if (NovelSource.NS03.getCode().equals(novel.getSource())) {
            // qu.la
            Document document = HtmlUtil.parseUrl(NovelSource.NS03.getUrl() + "book/" + novel.getCode());
            return document.select("#list dd a");
        } else if (NovelSource.NS04.getCode().equals(novel.getSource())) {
            // 800txt
            Document document = HtmlUtil.parseUrl(NovelSource.NS04.getUrl() + "book_" + novel.getCode());
            return document.select("#list dd a");
        } else if (NovelSource.NS05.getCode().equals(novel.getSource())) {
            // xianqihaotianmi
            Document document = HtmlUtil.parseUrl(NovelSource.NS05.getUrl() + "book/" + novel.getCode() + ".html");
            return document.select(".list-charts li a");
        } else if (NovelSource.NS06.getCode().equals(novel.getSource())) {
            // yuanzunxs
            Document document = HtmlUtil.parseUrl(NovelSource.NS06.getUrl() + "go/" + novel.getCode() + "/");
            return document.select("#list-chapterAll dd a");
        } else if (NovelSource.NS07.getCode().equals(novel.getSource())) {
            // 63xs
            Document document = HtmlUtil.parseUrl(NovelSource.NS07.getUrl() + "book/" + novel.getCode() + "/");
            return document.select("#list dd a");
        } else if (NovelSource.NS08.getCode().equals(novel.getSource())) {
            // 2K小说
            Document document = HtmlUtil.parseUrl(NovelSource.NS08.getUrl() + "xiaoshuo/" + novel.getCode() + "/");
            return document.select("dl.book dd a");
        } else {
            log.error("未知小说源, name={}, source={}", novel.getName(), novel.getSource());
        }
        return null;
    }

}
