package com.cc.lab_teach.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate redisTemplate;

    public void viewspp() {
        Long views = (Long) redisTemplate.opsForValue().get("views");
        if (views == null) views = 0l;
        redisTemplate.opsForValue().set("views", views + 1);
        LOG.info("今日访问量为: {}", views + 1);
    }

    public void setToken(String token, String content) {
        redisTemplate.opsForValue().set(token, content, 3600 * 24, TimeUnit.SECONDS); // 操作什么？即，往 redis 里 set 一个值
    }
}
