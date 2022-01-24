package com.cc.lab_teach.controller;

import com.cc.lab_teach.req.AddStudentReq;
import com.cc.lab_teach.req.StudentReq;
import com.cc.lab_teach.resp.AllStudent;
import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.resp.StudentResp;
import com.cc.lab_teach.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
        CommonResp<StudentResp> resp = new CommonResp<>();
        resp.setContent(login);
        resp.setMessage("登录成功");
        LOG.info("登录成功: {}", login);
        return resp;
    }

    @GetMapping("/addStudent/{c_id}")
    public CommonResp<Boolean> addStudent(AddStudentReq student, @PathVariable String c_id) {
        studentService.addStudent(student, Integer.parseInt(c_id));
        CommonResp<Boolean> resp = new CommonResp<>();
        resp.setContent(true);
        resp.setMessage("添加成功");
        return resp;
    }

    @GetMapping("/getAllStudents/{c_id}")
    public CommonResp<List<AllStudent>> getAllStudents(@PathVariable String c_id) {
        LOG.info("有教师在请求, c_id = {} 的班级信息", c_id);
        List<AllStudent> students = studentService.getAllStudents(Integer.parseInt(c_id));
        CommonResp<List<AllStudent>> resp = new CommonResp<>();
        resp.setContent(students);
        resp.setMessage("查询成功");
        return resp;
    }
}
