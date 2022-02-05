package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Snapshot;
import com.cc.lab_teach.mapper.MyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SnapshotService {
    private static final Logger LOG = LoggerFactory.getLogger(SnapshotService.class);

    @Resource
    private MyMapper myMapper;

    public List<Snapshot> getSnapshot() {
        LOG.info("开始查询");
        return myMapper.get30DayStatistic();
    }
}
