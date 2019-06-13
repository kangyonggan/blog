package com.kangyonggan.blog.service.impl.sites;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.dto.ArticleRequest;
import com.kangyonggan.blog.model.Article;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.ArticleService;
import com.kangyonggan.blog.util.Aes;
import com.kangyonggan.blog.util.DateUtil;
import com.kangyonggan.blog.util.MarkdownUtil;
import com.kangyonggan.blog.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-08
 */
@Service
@Log4j2
public class ArticleServiceImpl extends BaseService<Article> implements ArticleService {

    /**
     * 文件跟路径
     */
    @Value("${app.file-upload}")
    private String fileUploadPath;

    @Value("${app.aes-key}")
    private String aesKey;

    @Value("${app.aes-iv}")
    private String aesIv;

    @Override
    @MethodLog
    public List<Article> searchArticles(ArticleRequest articleRequest) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();

        String title = articleRequest.getTitle();
        if (StringUtils.isNotEmpty(title)) {
            criteria.andLike("title", StringUtil.toLike(title));
        }

        String[] createdTime = articleRequest.getCreatedTime();
        if (createdTime != null && StringUtils.isNotEmpty(createdTime[0])) {
            criteria.andGreaterThanOrEqualTo("createdTime", createdTime[0]);
            criteria.andLessThanOrEqualTo("createdTime", createdTime[1]);
        }

        if (StringUtils.isNotEmpty(articleRequest.getSort())) {
            if (articleRequest.getOrder() == 0) {
                example.orderBy(articleRequest.getSort()).asc();
            } else {
                example.orderBy(articleRequest.getSort()).desc();
            }
        } else {
            example.orderBy("createdTime").desc();
        }

        PageHelper.startPage(articleRequest.getPageNum(), articleRequest.getPageSize());
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public List<Article> searchArticles(Integer pageNum, Integer pageSize, String key) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());
        if (StringUtils.isNotEmpty(key)) {
            criteria.andLike("title", StringUtil.toLike(key));
        }

        example.selectProperties("articleId", "title", "summary", "viewNum", "createdTime", "content");
        example.orderBy("createdTime").desc();
        PageHelper.startPage(pageNum, pageSize);

        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public List<Article> preSearchArticles(String key) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());
        if (StringUtils.isNotEmpty(key)) {
            criteria.andLike("title", StringUtil.toLike(key));
        }

        example.selectProperties("articleId", "title");
        example.orderBy("createdTime").desc();
        PageHelper.startPage(1, 13);
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public List<Article> findViewArticles() {
        Example example = new Example(Article.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        example.selectProperties("articleId", "title", "summary", "viewNum", "createdTime");
        example.orderBy("viewNum").desc();
        PageHelper.startPage(1, 20);

        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public Article findArticleById(Long articleId) {
        return myMapper.selectByPrimaryKey(articleId);
    }

    @Override
    @MethodLog
    public void updateArticle(Article article) {
        myMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    @MethodLog
    public void saveArticle(Article article) {
        myMapper.insertSelective(article);
    }

    @Override
    public void genRss() throws Exception {
        Example example = new Example(Article.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        List<Article> articles = myMapper.selectByExample(example);

        genRssFile(articles);
    }

    private void genRssFile(List<Article> articles) throws Exception {

        StringBuilder rss = new StringBuilder("<feed xmlns=\"http://www.w3.org/2005/Atom\"><title>东方骄子</title>");
        rss.append("<link href=\"/upload/rss.xml\" rel=\"self\"/>").append("<link href=\"https://www.kangyonggan.com/\"/>");
        rss.append("<updated>").append(DateUtil.toXmlDateTime(new Date())).append("</updated>");
        rss.append("<id>https://www.kangyonggan.com/</id>");
        rss.append("<author><name>康永敢</name></author>");

        for (Article article : articles) {
            String articleId = Aes.encrypt(article.getArticleId() + "", aesKey, aesIv);
            rss.append("<entry><title>").append(article.getTitle()).append("</title>");
            rss.append("<link href=\"https://www.kangyonggan.com/article/").append(articleId).append("\"/>");
            rss.append("<id>https://www.kangyonggan.com/article/").append(articleId).append("</id>");
            rss.append("<published>").append(DateUtil.toXmlDateTime(article.getCreatedTime())).append("</published>");
            rss.append("<updated>").append(DateUtil.toXmlDateTime(article.getUpdatedTime())).append("</updated>");
            rss.append("<content type=\"html\"><![CDATA[").append(MarkdownUtil.markdownToHtml(article.getContent())).append("]]></content>");
            rss.append("<summary type=\"html\"><![CDATA[").append(article.getSummary()).append("]]></summary>");

            rss.append("<category term=\"全部文章\" scheme=\"https://www.kangyonggan.com/article/\"/>");
            rss.append("</entry>");
        }

        rss.append("</feed>");

        File file = new File(fileUploadPath + "rss.xml");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(rss.toString());
            writer.flush();
        } catch (Exception e) {
            log.error("生成博客rss异常, 文件路径：" + fileUploadPath + "rss.xml", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
