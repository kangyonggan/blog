package com.kangyonggan.blog;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 给配置文件中的密码加密
 *
 * @author kangyonggan
 * @since 2019-06-25
 */
public class JasyptTest extends AbstractTest {

    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    public void testEncrypt() {
        String data = "123456";
        logger.info("{}加密后：{}", data, stringEncryptor.encrypt(data));
    }
}
