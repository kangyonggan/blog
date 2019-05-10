---
title: 境外支付平台PayPal接口的开发过程
date: 2018-10-26 16:47:19
categories: Java后台
tags:
- Java
---


## 名词定义
- 用户：在我们网站上交易的客户。
- 支付网关：是我们商户和PayPal对接的系统。
- 商户系统：充值成功后用于发货的系统。
- PayPal：是海外的一家支付平台。

## PayPal官网地址
官网：[https://paypal.com](https://paypal.com)
开发者网站：[https://developer.paypal.com](https://developer.paypal.com)
沙盒地址：[https://www.sandbox.paypal.com/](https://www.sandbox.paypal.com/)
Demo地址：[https://demo.paypal.com](https://demo.paypal.com)

<!-- more -->

## 序列图
用户登录我们的网站，选择好商品之后，点击使用PayPal付款，直到用户户看到付款成功。对应的序列图如下：
![](/upload/article/paypal00.png)

我们就是要做上图这么一件事，下面是详细的开发步骤。

## 一、注册个人账号
注册地址：[https://www.paypal.com/us/webapps/mpp/account-selection](https://www.paypal.com/us/webapps/mpp/account-selection)

选择`个人账户`即可。

![](/upload/article/paypal01.png)
![](/upload/article/paypal02.png)

只需按部就班填写即可，此处不再截图注册流程。

## 二、创建测试账号
开发者网站：[https://developer.paypal.com/](https://developer.paypal.com/)
用上面注册的账户登录开发者网站，在工作台上创建一个商户账号和一个买家账号，用于开发和测试。
![](/upload/article/paypal03.png)

在创建买家账户的时候，别忘了给买家账户里多加一些钱。

## 三、获取clientId和secret
在调用REST API时要在请求头中加入token，而token是用clientId和secret获取的。

![](/upload/article/paypal04.png)
![](/upload/article/paypal05.png)
## 四、获取token
```
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 10/24/18
 */
@Component
@Log4j2
public class PayPalToken {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secret;

    @Value("${paypal.tokenUrl}")
    private String tokenUrl;

    private String token;

    private long expire;

    /**
     * 获取Token
     *
     * @return
     * @throws Exception
     */
    public String getToken() throws Exception {
        synchronized (this) {
            if (expire < System.currentTimeMillis()) {
                refreshToken();
            }
            return token;
        }
    }

    /**
     * 刷新Token
     *
     * @throws Exception
     */
    private void refreshToken() throws Exception {
        Map<String, String> header = new HashMap<>(16);
        header.put("Accept", "application/json");
        header.put("Accept-Language", "en_US");
        header.put("content-type", "application/x-www-form-urlencoded");
        byte[] authEncBytes = Base64.encodeBase64((clientId + ":" + secret).getBytes());
        String authStringEnc = new String(authEncBytes);
        header.put("Authorization", "Basic " + authStringEnc);
        log.info("获取token的请求头:{}", header);

        String result = HttpUtil.send(tokenUrl, header, "grant_type=client_credentials");
        log.info("获取token的响应:{}", result);
        if (StringUtils.isEmpty(result)) {
            throw new Exception("获取token失败");
        }

        JSONObject jsonObject = JSON.parseObject(result);
        this.token = jsonObject.getString("access_token");
        this.expire = System.currentTimeMillis() + jsonObject.getInteger("expires_in") / 3 * 1000;
    }
}
```

其中tokenUrl为：https://api.sandbox.paypal.com/v1/oauth2/token
对应接口文档地址：[https://developer.paypal.com/docs/api/overview/#make-your-first-call](https://developer.paypal.com/docs/api/overview/#make-your-first-call)

注意：token具有时效性。

## 五、预交易（下单）
这是一个预交易接口，即调用后不会真正的发生金钱交易，只是告诉PayPal有个用户一会要支付1000元买手机，你给我生成一个支付链接，我把这个链接给用户，让他去到你网站支付。调用此接口后会返回支付链接和查询此交易的查询链接。

```
/**
 * PayPal预支付
 *
 * @param command
 * @return
 */
public CommonResponse pay(Command command) {
    log.info("进入PayPal预支付接口");
    PayResponse payResponse = PayResponse.getResponse();
    payResponse.setNeedTranslate(false);

    try {
        PaymentRequest paymentRequest = new PaymentRequest(command, returnUrl, cancelUrl);
        String body = JSON.toJSONString(paymentRequest);
        log.info("PayPal预支付接口请求参数:{}", body);

        Map<String, String> header = new HashMap<>(16);
        header.put("PayPal-Request-Id", command.getSerialNo());
        header.put("content-type", "application/json");
        header.put("cache-control", "no-cache");
        header.put("Authorization", "Bearer " + payPalToken.getToken());

        String result = HttpUtil.send(payUrl, header, body);
        log.info("PayPal响应报文:{}", result);
        if (StringUtils.isEmpty(result)) {
            throw new SendException("PayPal响应报文为空");
        }
        JSONObject jsonObject = JSON.parseObject(result);

        payResponse.setApiUrl(jsonObject.getJSONArray("links").getJSONObject(1).getString("href"));
        payResponse.setChannelSerialNo(jsonObject.getString("id"));
        payResponse.setTranSt(TranSt.I.getCode());

        payResponse.setRespData(result);
    } catch (Exception e) {
        throw new SendException("请求PayPal预支付接口异常", e);
    }

    log.info("离开PayPal预支付接口");
    return payResponse;
}
```

`PaymentRequest`是一个实体类，和接口文档中的请求json结构一致。
`returnUrl`是用户确认付支付后，PayPal向我们商户跳转的连接，PayPal会带有一些参数，同时我们也可以把我们的流水号放进去。
`cancelUrl`是用户在paypal界面点击取消付款后，paypal想我们商户跳转的连接。

`HttpUtil.java`   
```
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 10/24/18
 */
public final class HttpUtil {

    private HttpUtil() {

    }

    /**
     * 发送http请求
     *
     * @param url
     * @param header
     * @param body
     * @return
     * @throws Exception
     */
    public static String send(String url, Map<String, String> header, String body) throws Exception {
        StringBuilder result = new StringBuilder();
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            if (header != null) {
                for (String key : header.keySet()) {
                    conn.setRequestProperty(key, header.get(key));
                }
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (StringUtils.isNotEmpty(body)) {
                out = new PrintWriter(conn.getOutputStream());
                out.print(body);
                out.flush();
            }
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }
}
```

到此，用户已经拿到了付款连接，可以跳转到paypal进行付款了。

对应接口文档地址：[https://developer.paypal.com/docs/api/payments/v1/#payment_create](https://developer.paypal.com/docs/api/payments/v1/#payment_create)

## 六、执行付款
当用户在paypal界面完成付款后，paypal就会重定向到我们送给他的`returnUrl`：

```
/**
 * PayPal充值成功
 *
 * @param serialNo 流水号
 * @param model
 * @param paymentId
 * @param PayerID
 * @return
 */
@GetMapping("return")
public String paypalReturn(String serialNo, String paymentId, String PayerID, Model model) {
    // TODO 简单校验， 比如校验一下流水号是不是我们的
    
    // 执行付款
    try {
        String url = executeUrl + "/" + paymentId + "/execute/";
        Map<String, String> header = new HashMap<>(8);
        header.put("PayPal-Request-Id", serialNo);
        header.put("content-type", "application/json");
        header.put("Authorization", "Bearer " + payPalToken.getToken());
        result = HttpUtil.send(url, header, "{\"payer_id\":\"" + payerID + "\"}");
        log.info("执行付款响应结果:{}", result);

        JSONObject jsonObject = JSON.parseObject(result);
        String state = jsonObject.getString("state");
        if ("failed".equals(state)) {
            log.info("PayPal执行付款失败");
            // TODO 塞点数据给界面
            return "paypal/failure";
        } else if ("approved".equals(state)) {
            log.info("PayPal执行付款成功");
        }
    } catch (Exception e) {
        log.error("PayPal执行付款异常", e);
        // TODO 塞点数据给界面
        return "paypal/error";
    }
    // TODO 塞点数据给界面
    return "paypal/return";
}
```

其中executeUrl为：https://api.sandbox.paypal.com/v1/payments/payment

注意：不能在此方法内调用商户系统进行发货，因为这个请求可能不是paypal发来的，我们应该是后台通知时再通知商户系统发货。

对应的接口文档地址：[https://developer.paypal.com/docs/api/payments/v1/#payment_execute](https://developer.paypal.com/docs/api/payments/v1/#payment_execute)

## 七、后台通知
可以使用卖家账户登录沙盒环境，点击”卖家习惯设定“ > "收款和管理我的风险" > "即时付款通知"。
直达链接：[https://www.sandbox.paypal.com/c2/cgi-bin/webscr?cmd=_profile-display-handler&tab_id=SELLER_PREFERENCES](https://www.sandbox.paypal.com/c2/cgi-bin/webscr?cmd=_profile-display-handler&tab_id=SELLER_PREFERENCES)

![](/upload/article/paypal06.png)

```
/**
 * PayPal后台通知
 *
 * @param request
 * @throws Exception
 */
@PostMapping("paypal")
public void paypal(HttpServletRequest request) throws Exception {
    log.info("进入PayPal后台通知");

    // 获取paypal请求参数,并拼接验证参数
    Enumeration<String> en = request.getParameterNames();
    StringBuilder data = new StringBuilder("cmd=_notify-validate");
    while (en.hasMoreElements()) {
        String paramName = en.nextElement();
        String paramValue = request.getParameter(paramName);
        data.append("&").append(paramName).append("=").append(URLEncoder.encode(paramValue, "UTF-8"));
    }
    log.info("收到PayPal通知为：{}", data);

    // 校验
    URL u = new URL(checkUrl);
    HttpURLConnection uc = (HttpURLConnection) u.openConnection();
    uc.setRequestMethod("POST");
    uc.setDoOutput(true);
    uc.setDoInput(true);
    uc.setUseCaches(false);
    //设置 HTTP 的头信息
    uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    PrintWriter pw = new PrintWriter(uc.getOutputStream());
    pw.println(data.toString());
    pw.close();
    log.info("已发往PayPal进行回调校验");

    // 响应
    BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
    String result = in.readLine();
    in.close();
    log.info("收到的交易结果为：{}", result);

    if ("VERIFIED".equals(result)) {
        log.info("验证通过");
        String serialNo = request.getParameter("invoice");
        Command command = commandService.findCommandBySerialNo(serialNo);
        if (command == null) {
            log.info("PayPal通知流水号不存在:{}", serialNo);
            return;
        }

        String status = request.getParameter("payment_status");
        if ("Completed".equals(status)) {
            log.info("交易成功，通知商户系统发货");
            boolean success = paymentHelper.notify(command);

            // 如果不通知成功，交易状态不能更新为成功
            if (success) {
                // 响应落库, 更新交易状态为成功
                payPalTransactionService.processNotice(status, command, data.toString());
            }
        }
    } else {
        log.error("收到非法PayPal通知：{}", data);
    }
}
```

其中checkUrl：https://www.sandbox.paypal.com/cgi-bin/webscr

对应接口文档地址：[https://www.paypal.com/us/cgi-bin/webscr?cmd=p/acc/ipn-info-outside](https://www.paypal.com/us/cgi-bin/webscr?cmd=p/acc/ipn-info-outside)

## 八、单笔查询
如果我们没收到后台通知怎么办？这时候就可以通过单笔查询主动去paypal查询订单状态。

```
Map<String, String> header = new HashMap<>(8);
header.put("Content-Type", "application/json");
header.put("Authorization", "Bearer " + payPalToken.getToken());

String result = HttpUtil.send(queryUrl + "/" + paymentId, header, null);
log.info("查询的响应:{}", result);

if (StringUtils.isNotEmpty(result)) {
    log.info("查询得到结果");
    JSONObject jsonObject = JSON.parseObject(result);
    String state = jsonObject.getString("state");

    log.info("交易状态:{}", state);
    if ("approved".equals(state)) {
        log.info("交易成功");
    } else if ("failed".equals(respCo)) {
        log.info("交易失败");
    } else {
        log.info("交易处理中");
    }
} else {
    log.info("查询失败");
}
```

其中queryUrl：https://api.sandbox.paypal.com/v1/payments/payment
paymentId是在预交易接口返回的。

对应接口文档地址：[https://developer.paypal.com/docs/api/payments/v1/#payment_get](https://developer.paypal.com/docs/api/payments/v1/#payment_get)

## 九、批量查询
如果我们需要日终对账，那么就要用到批量查询接口。但是此接口不大符合我的需要，因此我没用这个接口，而是循环使用单笔查询代替的。

对应接口文档地址：[https://developer.paypal.com/docs/api/payments/v1/#payment_list](https://developer.paypal.com/docs/api/payments/v1/#payment_list)
