package com.tensquare.test;

import com.tensquare.rabbit.RabbitApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitApplication.class)
public class ProductTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSend() {
        rabbitTemplate.convertAndSend("itcast", "我要红包");
    }

    /**
     * 分列模式
     */
    @Test
    public void testFunout() {
        rabbitTemplate.convertAndSend("chuanzhi", "", "分列模式");
    }

    /**
     * 话题模式
     */
    @Test
    public void testTopic() {
        rabbitTemplate.convertAndSend("topic22", "abc.log", "订阅模式");
    }

}
