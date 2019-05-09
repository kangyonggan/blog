package com.kangyonggan.blog.service.sites;


import com.kangyonggan.blog.model.Section;

import java.util.List;

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

    /**
     * 查找最新九章
     *
     * @param novelId
     * @return
     */
    List<Section> findLastSections(Long novelId);

    /**
     * 查找全部章节
     *
     * @param novelId
     * @return
     */
    List<Section> findSections(Long novelId);

    /**
     * 查找章节
     *
     * @param sectionId
     * @return
     */
    Section findSectionById(Long sectionId);

    /**
     * 查找上一章节
     *
     * @param novelId
     * @param sectionId
     * @return
     */
    Section findPrevSection(Long novelId, Long sectionId);

    /**
     * 查找下一章节
     *
     * @param novelId
     * @param sectionId
     * @return
     */
    Section findNextSection(Long novelId, Long sectionId);
}
