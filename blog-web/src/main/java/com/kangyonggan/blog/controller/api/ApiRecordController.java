package com.kangyonggan.blog.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Record;
import com.kangyonggan.blog.service.wap.RecordService;
import com.kangyonggan.blog.util.HttpUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kangyonggan
 * @since 9/25/18
 */
@RestController
@RequestMapping("wx")
@Secret(enable = false)
@Log4j2
public class ApiRecordController extends BaseController {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Autowired
    private RecordService recordService;

    /**
     * 获取微信的openid
     *
     * @param jsCode
     * @return
     */
    @GetMapping("getOpenId")
    public Response getOpenId(String jsCode) {
        Response response = successResponse();
        try {
            String res = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session",
                    "appid=" + appid + "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=authorization_code");
            log.info("获取openid响应：{}", res);
            JSONObject jsonObject = JSONObject.parseObject(res);
            response.put("openid", jsonObject.getString("openid"));
            response.put("sessionKey", jsonObject.getString("session_key"));
        } catch (Exception e) {
            log.error("获取openid异常", e);
            response.failure("获取openid异常");
        }

        return response;
    }

    /**
     * 保存记录
     *
     * @param record
     * @return
     */
    @PostMapping("saveRecord")
    public Response saveRecord(@RequestBody Record record) {
        recordService.saveRecord(record);
        return successResponse();
    }

    /**
     * 查询记录
     *
     * @param openid
     * @param pageNum
     * @return
     */
    @GetMapping("records")
    public Response records(@RequestParam(value = "openid", required = false, defaultValue = "") String openid,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
        Response response = successResponse();
        List<Record> records = recordService.findRecords(openid, pageNum);

        response.put("pageInfo", new PageInfo<>(records));
        return response;
    }

    /**
     * 记录详情
     *
     * @param id
     * @return
     */
    @GetMapping("record/{id:[\\d]+}")
    public Response records(@PathVariable("id") Long id) {
        Response response = successResponse();
        Record record = recordService.findRecordById(id);

        response.put("record", record);
        return response;
    }

}
