package com.kangyonggan.blog.service.sites;


import com.kangyonggan.blog.model.Section;

/**
 * @author kangyonggan
 * @since 2019/1/5 0005
 */
public interface SectionService {

    /**
     * 查找最新章节
     *
     * @param novelId
     * @return
     */
    Section findLastSection(Long novelId);

    /**
     * 保存章节
     *
     * @param section
     */
    void saveSection(Section section);

}
