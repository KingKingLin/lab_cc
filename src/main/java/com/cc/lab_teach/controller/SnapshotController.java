package com.cc.lab_teach.controller;

import com.cc.lab_teach.domain.Snapshot;
import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.service.SnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class SnapshotController {
    private static final Logger LOG = LoggerFactory.getLogger(SnapshotController.class);

    @Resource
    private SnapshotService snapshotService;

    @GetMapping("/snapshot")
    public CommonResp<List<Snapshot>> getSnapshot() {
        LOG.info("查询近期30天的访问量");
        List<Snapshot> result = snapshotService.getSnapshot();
        CommonResp<List<Snapshot>> resp = new CommonResp<>();
        resp.setContent(result);
        resp.setMessage("查询成功");
        return resp;
    }

}
