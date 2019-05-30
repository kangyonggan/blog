package com.kangyonggan.blog.service.system;

import com.kangyonggan.blog.dto.DictRequest;
import com.kangyonggan.blog.model.Dict;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-04-02
 */
public interface DictService {

    /**
     * 保存字典
     *
     * @param dict
     */
    void saveDict(Dict dict);

    /**
     * 更新字典
     *
     * @param dict
     */
    void updateDict(Dict dict);

    /**
     * 校验字典代码是否存在
     *
     * @param dictType
     * @param dictCode
     * @return
     */
    boolean existsDictCode(String dictType, String dictCode);

    /**
     * 根据字典类型查找字典
     *
     * @param dictType
     * @return
     */
    List<Dict> findDictsByDictType(String dictType);

    /**
     * 搜索字典
     *
     * @param dictRequest
     * @return
     */
    List<Dict> searchDicts(DictRequest dictRequest);
}
