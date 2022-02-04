package com.cc.lab_teach.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SnapshotJob {
    private static final Logger LOG = LoggerFactory.getLogger(SnapshotJob.class);

    /**
     * corn 每天的 23:59:59 更新快照表
     */
    @Scheduled(cron = "59 59 23 * * ? ")
    public void cron() {
        LOG.info("23:59:59 更新快照表开始");
        long start = System.currentTimeMillis();
//        docService.updateEbookInfo();
        LOG.info("更新快照表数据结束，耗时：{} 毫秒", System.currentTimeMillis() - start);
    }
}
