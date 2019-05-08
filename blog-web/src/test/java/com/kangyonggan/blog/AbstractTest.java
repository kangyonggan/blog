package com.kangyonggan.blog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author kangyonggan
 * @since 7/19/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.kangyonggan.blog.mapper")
@PropertySource(value = "classpath:app-dev.properties", encoding = "UTF-8")
public abstract class AbstractTest {

    protected Logger logger = LogManager.getLogger(AbstractTest.class);

}
