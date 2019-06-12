package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.util.IdNoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("validate")
@Api(tags = "ValidateController", description = "校验相关接口")
public class ValidateController extends BaseController {

    /**
     * 校验证件号码是否合法
     *
     * @param idNo
     * @return
     */
    @PostMapping("idNo")
    @ApiOperation("校验证件号码是否合法")
    @ApiImplicitParam(name = "idNo", value = "证件号码", required = true, example = "340321199103173095")
    public Response idNo(String idNo) {
        Response response = successResponse();
        idNo = idNo.replaceAll("x", "X");

        if (!IdNoUtil.isIdCard(idNo)) {
            response.failure("身份证号码不合法");
        }

        return response;
    }

}
