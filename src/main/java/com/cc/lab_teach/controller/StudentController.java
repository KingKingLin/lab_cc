package com.cc.lab_teach.controller;

import com.cc.lab_teach.req.AddStudentReq;
import com.cc.lab_teach.req.PageReq;
import com.cc.lab_teach.req.StudentReq;
import com.cc.lab_teach.req.TeacherReq;
import com.cc.lab_teach.resp.*;
import com.cc.lab_teach.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    @GetMapping("/add/{c_id}")
    public CommonResp<Boolean> addStudent(AddStudentReq student, @PathVariable String c_id) {
        studentService.addStudent(student, Integer.parseInt(c_id));
        CommonResp<Boolean> resp = new CommonResp<>();
        resp.setContent(true);
        resp.setMessage("添加成功");
        return resp;
    }

    @GetMapping("/all/{c_id}")
    public CommonResp<List<StudentPageResp>> getAllStudents(@PathVariable String c_id) {
        LOG.info("有教师在请求, c_id = {} 的所有班级信息", c_id);
        List<StudentPageResp> students = studentService.getAllStudents(Integer.parseInt(c_id));
        CommonResp<List<StudentPageResp>> resp = new CommonResp<>();
        resp.setContent(students);
        resp.setMessage("查询成功");
        return resp;
    }

    @GetMapping("/part/{c_id}")
    public CommonResp<PageResp<StudentPageResp>> getPartStudents(PageReq page, @PathVariable String c_id) {
        LOG.info("有教师在请求, c_id = {} 的部分班级信息", c_id);
        PageResp<StudentPageResp> students = studentService.getStudentByPage(page, Integer.parseInt(c_id));
        CommonResp<PageResp<StudentPageResp>> resp = new CommonResp<>();
        resp.setContent(students);
        resp.setMessage("查询成功");
        return resp;
    }

    // @Valid 开启参数校验
    @PostMapping("/reset-password")
    public CommonResp<Object> resetPassword(@Valid StudentResp student) {
        LOG.info("{} 请求修改密码", student);
        studentService.resetPassword(student);
        CommonResp<Object> result = new CommonResp<>();
        result.setMessage("修改密码成功");
        LOG.info("{} 修改密码成功", student);
        return result;
    }

    @GetMapping("/all/experiments")
    public CommonResp<List<ExperimentResp>> getAllExperiments(String id) {
        LOG.info("学生 {} 想要查询与其有关的所有实验", id);
        List<ExperimentResp> result = studentService.getAllExperiments(id);
        CommonResp<List<ExperimentResp>> resp = new CommonResp<>();
        resp.setContent(result);
        resp.setMessage("查询成功");
        return resp;
    }
}
