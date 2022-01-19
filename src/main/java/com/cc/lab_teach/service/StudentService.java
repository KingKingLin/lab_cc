package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentService {
    @Resource
    private StudentMapper studentMapper;

    public List<Student> selectStudents() {
        List<Student> students = studentMapper.selectByExample(null);
        return students;
    }
}
