package com.kangyonggan.blog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author kangyonggan
 * @since 7/19/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractTest {

    protected Logger logger = LogManager.getLogger(AbstractTest.class);

}
