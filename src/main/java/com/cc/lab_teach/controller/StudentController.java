package com.cc.lab_teach.controller;

import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class StudentController {
    @Resource
    private StudentService studentService;

    @GetMapping("/students")
    public List<Student> getStudents() {
        List<Student> students = studentService.selectStudents();
        return students;
    }
}
