package com.cc.lab_teach.controller;

import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.resp.ExperimentResp;
import com.cc.lab_teach.service.ExperimentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class ExperimentController {
    private static final Logger LOG = LoggerFactory.getLogger(ExperimentController.class);

    @Resource
    private ExperimentService experimentService;

    @GetMapping("/all-experiments/{c_id}")
    public CommonResp<List<ExperimentResp>> getAllExperiments(@PathVariable String c_id) {
        LOG.info("有人请求班级 id = {}", c_id);
        List<ExperimentResp> result = experimentService.getAllExperiments(Integer.parseInt(c_id));
        CommonResp<List<ExperimentResp>> resp = new CommonResp<>();
        resp.setContent(result);
        resp.setMessage("查询成功");
        return resp;
    }

    @GetMapping("/add-experiment/{c_id}")
    public CommonResp<Boolean> addExperiment(String title, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date deadline, @PathVariable String c_id) {
        LOG.info("班级 id = {} 请求新建 【{}】", c_id, title);
        experimentService.addExperiment(Integer.parseInt(c_id), title, deadline);
        CommonResp<Boolean> resp = new CommonResp<>();
        resp.setContent(true);
        resp.setMessage("班级 " + c_id + " 创建实验 【" + title + "】 成功");
        return resp;
    }
}
