package com.kangyonggan.blog.service.system;

import com.kangyonggan.blog.model.Dict;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-04-02
 */
public interface DictService {

    /**
     * 删除字典
     *
     * @param dictId
     */
    void deleteDict(Long dictId);

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
     * 查找字典
     *
     * @param dictType
     * @param dictCode
     * @return
     */
    Dict findDictByTypeAndCode(String dictType, String dictCode);
}
