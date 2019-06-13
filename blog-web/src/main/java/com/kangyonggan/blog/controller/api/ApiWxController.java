package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.dto.AutoReplyRequestDto;
import com.kangyonggan.blog.service.api.WxService;
import com.kangyonggan.blog.util.IoUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
@RestController
@RequestMapping("api/wx")
@Log4j2
@Secret(enable = false)
public class ApiWxController {

    @Autowired
    private WxService wxService;

    /**
     * 自动回复
     *
     * @return
     * @throws Exception
     */
    @PostMapping
    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("========= 微信公告号后台收到一个请求 =========");

        AutoReplyRequestDto requestDto = wxService.getRequestDtoFromRequest(request.getInputStream());
        if (requestDto == null) {
            return "error";
        }

        String respXml = wxService.getResponseXml(requestDto);
        log.info("响应报文：{}", respXml);

        IoUtil.write(response.getOutputStream(), respXml);
        return "success";
    }

    /**
     * 验证开发者服务器
     *
     * @return
     */
    @GetMapping
    public String validate(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        log.info("signature:" + signature);
        log.info("timestamp:" + timestamp);
        log.info("nonce:" + nonce);
        log.info("echostr:" + echostr);
        return echostr;
    }

}
