package com.cc.lab_teach.controller;

import com.cc.lab_teach.req.PageReq;
import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.resp.ExperimentResp;
import com.cc.lab_teach.resp.PageResp;
import com.cc.lab_teach.resp.StudentDetailResp;
import com.cc.lab_teach.service.ExperimentService;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    /**
     * 日期类型数据的传递，前端都是以 String 类型返回的
     *
     * 表的数据：
     * java.util.Date 存入数据库 只有日期，没有时间
     * java.sql.Time 存入数据库 只有时间，没有日期
     * 解决方法：java.sql.Timestamp
     *
     * 其中 @DateTimeFormat 和 @JsonFormat 缺一不可，后端只能接受前端是以 "yyyy-MM-dd HH:mm:ss" 格式传递过来的时间数据
     *      timezone 表示时区，中国 和 标准时区 差 8 个小时
     */
    @GetMapping("/add-experiment/{c_id}")
    public CommonResp<Boolean> addExperiment(String title,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                             @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
                                                     Date deadline,
                                             @PathVariable String c_id) {
        LOG.info("班级 id = {} 请求新建 【{}】", c_id, title);
        experimentService.addExperiment(Integer.parseInt(c_id), title, deadline);
        CommonResp<Boolean> resp = new CommonResp<>();
        resp.setContent(true);
        resp.setMessage("班级 " + c_id + " 创建实验 【" + title + "】 成功");
        return resp;
    }

    @GetMapping("/get-details")
    public CommonResp<PageResp<StudentDetailResp>> getDetails(int c_id, long e_id, PageReq page) {
        LOG.info("获取班级【{}】 所有学生实验的详细信息", c_id);
        PageResp<StudentDetailResp> result = experimentService.getDetails(c_id, e_id, page);
        CommonResp<PageResp<StudentDetailResp>> resp = new CommonResp<>();
        resp.setContent(result);
        resp.setMessage("查询成功");
        return resp;
    }

    @GetMapping("/get/experiment/{e_id}")
    public CommonResp<ExperimentResp> getExperiment(@PathVariable String e_id) {
        LOG.info("根据 e_id: {} 查询具体的实验信息", e_id);
        ExperimentResp result = experimentService.getExperiment(Long.parseLong(e_id));
        CommonResp<ExperimentResp> resp = new CommonResp<>();
        resp.setContent(result);
        resp.setMessage("查询成功");
        return resp;
    }
}
