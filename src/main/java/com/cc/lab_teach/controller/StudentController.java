package com.cc.lab_teach.controller;

import com.cc.lab_teach.req.StudentReq;
import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.resp.StudentResp;
import com.cc.lab_teach.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/student")
public class StudentController {
    private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);

    @Resource
    private StudentService studentService;

    @PostMapping("/login")
    public CommonResp<StudentResp> login(StudentReq student) {
        LOG.info("请求: {}", student);
        StudentResp login = studentService.login(student);
        CommonResp<StudentResp> result = new CommonResp<>();
        result.setContent(login);
        result.setMessage("登录成功");
        LOG.info("登录成功: {}", login);
        return result;
    }
}
