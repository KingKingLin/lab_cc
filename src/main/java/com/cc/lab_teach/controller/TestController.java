package com.cc.lab_teach.controller;

import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.server.TestServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private TestServer testServer;

    @GetMapping("/students")
    public List<Student> getStudents() {
        List<Student> students = testServer.getStudents();
        return students;
    }
}
