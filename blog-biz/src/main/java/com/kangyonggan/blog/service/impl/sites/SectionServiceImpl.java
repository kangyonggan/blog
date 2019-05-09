package com.kangyonggan.blog.service.impl.sites;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.model.Section;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.SectionService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-08
 */
@Service
public class SectionServiceImpl extends BaseService<Section> implements SectionService {

    @Override
    @MethodLog
    public Section findLastSection(Long novelId) {
        Example example = new Example(Section.class);
        example.createCriteria().andEqualTo("novelId", novelId);
        example.setOrderByClause("section_id desc");

        example.selectProperties("sectionId", "code", "title", "novelId", "createdTime");

        PageHelper.startPage(1, 1);
        List<Section> sections = myMapper.selectByExample(example);
        if (sections.isEmpty()) {
            return null;
        }
        return sections.get(0);
    }

    @Override
    public void saveSection(Section section) {
        myMapper.insertSelective(section);
    }

    @Override
    @MethodLog
    public List<Section> findLastSections(Long novelId) {
        Example example = new Example(Section.class);
        example.createCriteria().andEqualTo("novelId", novelId);
        example.setOrderByClause("section_id desc");

        example.selectProperties("sectionId", "title");

        PageHelper.startPage(1, 9);
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public List<Section> findSections(Long novelId) {
        Example example = new Example(Section.class);
        example.createCriteria().andEqualTo("novelId", novelId);
        example.setOrderByClause("section_id asc");

        example.selectProperties("sectionId", "novelId", "title");

        return myMapper.selectByExample(example);
    }
}
