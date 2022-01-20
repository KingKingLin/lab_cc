package com.cc.lab_teach.controller;

import com.cc.lab_teach.req.TeacherReq;
import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.resp.TeacherResp;
import com.cc.lab_teach.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private static final Logger LOG = LoggerFactory.getLogger(TeacherController.class);

    @Resource
    private TeacherService teacherService;

    @PostMapping("/login")
    public CommonResp<TeacherResp> login(TeacherReq teacher) {
        LOG.info("请求: {}", teacher);
        TeacherResp login = teacherService.login(teacher);
        CommonResp<TeacherResp> result = new CommonResp<>();
        result.setContent(login);
        result.setMessage("登录成功");
        LOG.info("登录成功: {}", login);
        return result;
    }
}
