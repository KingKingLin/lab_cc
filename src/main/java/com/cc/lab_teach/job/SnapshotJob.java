package com.cc.lab_teach.job;

import com.cc.lab_teach.mapper.MyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class SnapshotJob {
    private static final Logger LOG = LoggerFactory.getLogger(SnapshotJob.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MyMapper myMapper;

    /**
     * corn 每天的 04:00:00 更新快照表
     */
    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void cron() {
        LOG.info("04:00:00 更新快照表开始（昨天的访问量）");
        long start = System.currentTimeMillis();

        Long views = (Long) redisTemplate.opsForValue().get("views");
        if (views == null) views = 0l;
        // 更新数据
        myMapper.insertSnapshot(views);
        // 重置 views
        redisTemplate.opsForValue().set("views", 0l);
        LOG.info("更新快照表数据结束，耗时：{} 毫秒", System.currentTimeMillis() - start);
    }

   /* @Scheduled(cron = "0 0 0 * * ?")
    public void test() {
        LOG.info("00:00:00 重置访问量");
    }*/
}
