package test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JUnit 测试类基础配置
 *
 * @author wd
 * @date 2018/4/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring.xml"})
public class BaseTest extends AbstractJUnit4SpringContextTests {
}
