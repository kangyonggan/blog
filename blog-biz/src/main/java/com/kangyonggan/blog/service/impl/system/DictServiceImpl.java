package com.kangyonggan.blog.service.impl.system;

import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.model.Dict;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.system.DictService;
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
    public void deleteDict(Long dictId) {
        myMapper.deleteByPrimaryKey(dictId);
    }

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
    public Dict findDictByTypeAndCode(String dictType, String dictCode) {
        Dict dict = new Dict();
        dict.setDictType(dictType);
        dict.setDictCode(dictCode);
        dict.setIsDeleted(YesNo.NO.getCode());

        return myMapper.selectOne(dict);
    }
}
