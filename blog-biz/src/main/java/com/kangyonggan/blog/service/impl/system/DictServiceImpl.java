package com.kangyonggan.blog.service.impl.system;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.dto.DictRequest;
import com.kangyonggan.blog.model.Dict;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.system.DictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-04-02
 */
@Service
public class DictServiceImpl extends BaseService<Dict> implements DictService {

    @Override
    @MethodLog
    public void saveDict(Dict dict) {
        myMapper.insertSelective(dict);
    }

    @Override
    @MethodLog
    public void updateDict(Dict dict) {
        myMapper.updateByPrimaryKeySelective(dict);
    }

    @Override
    public boolean existsDictCode(String dictType, String dictCode) {
        Dict dict = new Dict();
        dict.setDictType(dictType);
        dict.setDictCode(dictCode);

        return super.exists(dict);
    }

    @Override
    @MethodLog
    public List<Dict> findDictsByDictType(String dictType) {
        Example example = new Example(Dict.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode()).andEqualTo("dictType", dictType);

        example.selectProperties("dictCode", "value");

        example.setOrderByClause("sort asc");
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public List<Dict> searchDicts(DictRequest dictRequest) {
        Example example = new Example(Dict.class);
        Example.Criteria criteria = example.createCriteria();

        String dictType = dictRequest.getDictType();
        if (StringUtils.isNotEmpty(dictType)) {
            criteria.andEqualTo("dictType", dictType);
        }

        String dictCode = dictRequest.getDictCode();
        if (StringUtils.isNotEmpty(dictCode)) {
            criteria.andEqualTo("dictCode", dictCode);
        }

        String[] createdTime = dictRequest.getCreatedTime();
        if (createdTime != null && StringUtils.isNotEmpty(createdTime[0])) {
            criteria.andGreaterThanOrEqualTo("createdTime", createdTime[0]);
            criteria.andLessThanOrEqualTo("createdTime", createdTime[1]);
        }

        if (StringUtils.isNotEmpty(dictRequest.getSort())) {
            if (dictRequest.getOrder() == 0) {
                example.orderBy(dictRequest.getSort()).asc();
            } else {
                example.orderBy(dictRequest.getSort()).desc();
            }
        } else {
            example.orderBy("dictId").desc();
        }

        PageHelper.startPage(dictRequest.getPageNum(), dictRequest.getPageSize());
        return myMapper.selectByExample(example);
    }
}
