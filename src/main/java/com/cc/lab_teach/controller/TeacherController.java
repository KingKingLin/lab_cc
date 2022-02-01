package com.cc.lab_teach.controller;

import com.cc.lab_teach.req.AnswerReq;
import com.cc.lab_teach.req.TeacherReq;
import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.resp.TeacherResp;
import com.cc.lab_teach.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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

    // @Valid 开启参数校验
    @PostMapping("/reset-password")
    public CommonResp<Object> resetPassword(@Valid TeacherReq teacher) {
        LOG.info("{} 请求修改密码", teacher);
        teacherService.resetPassword(teacher);
        CommonResp<Object> result = new CommonResp<>();
        result.setMessage("修改密码成功");
        LOG.info("{} 修改密码成功", teacher);
        return result;
    }

    @PostMapping("/put-correct")
    public CommonResp<Boolean> putCorrect(@RequestBody AnswerReq answer) {
        LOG.info("教师正在为学生 {} 评阅", answer.getsId());
        teacherService.putCorrect(answer);
        CommonResp<Boolean> resp = new CommonResp<>();
        resp.setContent(true);
        resp.setMessage("评阅成功");
        return resp;
    }
}
