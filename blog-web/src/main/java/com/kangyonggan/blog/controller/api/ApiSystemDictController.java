package com.kangyonggan.blog.controller.api;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.DictRequest;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Dict;
import com.kangyonggan.blog.service.system.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("api/system/dict")
@Api(tags = "ApiSystemDictController", description = "字典管理相关接口")
public class ApiSystemDictController extends BaseController {

    @Autowired
    private DictService dictService;

    /**
     * 字典列表查询
     *
     * @param dictRequest
     * @return
     */
    @PostMapping
    @ApiOperation("字典列表查询")
    @PermissionMenu("SYSTEM_DICT")
    public Response list(DictRequest dictRequest) {
        Response response = successResponse();

        List<Dict> list = dictService.searchDicts(dictRequest);
        PageInfo<Dict> pageInfo = new PageInfo<>(list);

        response.put("pageInfo", pageInfo);
        return response;
    }

    /**
     * 保存字典
     *
     * @param dict
     * @return
     */
    @PostMapping("save")
    @PermissionMenu("SYSTEM_DICT")
    @ApiOperation("保存字典")
    public Response save(Dict dict) {
        dictService.saveDict(dict);

        return successResponse();
    }

    /**
     * 更新字典
     *
     * @param dict
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新字典")
    @PermissionMenu("SYSTEM_DICT")
    public Response update(Dict dict) {
        dictService.updateDict(dict);

        return successResponse();
    }

    /**
     * 删除/恢复字典
     *
     * @param dictId
     * @param isDeleted
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除/恢复字典")
    @PermissionMenu("SYSTEM_DICT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictId", value = "字典ID", required = true, example = "1"),
            @ApiImplicitParam(name = "isDeleted", value = "是否删除(0:恢复，1:删除)", required = true, example = "0")
    })
    public Response delete(Long dictId, Byte isDeleted) {
        Dict dict = new Dict();
        dict.setDictId(dictId);
        dict.setIsDeleted(isDeleted);
        dictService.updateDict(dict);

        return successResponse();
    }

}
