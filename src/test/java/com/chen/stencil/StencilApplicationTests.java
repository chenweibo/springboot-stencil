package com.chen.stencil;

import com.chen.stencil.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StencilApplicationTests {

    @Autowired
    RedisUtils redisUtils;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSetRedis() {
        boolean status = redisUtils.set("testData", "EXMAPLE");
        String c = redisUtils.get("testData");
        System.out.println(c);
    }


}
