package com.kangyonggan.blog.service.impl.wap;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.constants.AppConstants;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.exception.BlogException;
import com.kangyonggan.blog.model.Record;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.wap.RecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 10/3/18
 */
@Service
public class RecordServiceImpl extends BaseService<Record> implements RecordService {

    @Override
    public void saveRecord(Record record) {
        if (StringUtils.isEmpty(record.getContent()) && StringUtils.isEmpty(record.getFileNames())) {
            throw new BlogException();
        }
        if (StringUtils.isEmpty(record.getContent())) {
            record.setContent("");
        }
        myMapper.insertSelective(record);
    }

    @Override
    public List<Record> findRecords(String openid, int pageNum) {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", YesNo.NO.getCode());
        if (StringUtils.isNotEmpty(openid)) {
            criteria.andEqualTo("openid", openid);
        }

        example.setOrderByClause("id desc");
        PageHelper.startPage(pageNum, AppConstants.SALT_SIZE);
        return myMapper.selectByExample(example);
    }

    @Override
    public Record findRecordById(Long id) {
        return myMapper.selectByPrimaryKey(id);
    }

}
