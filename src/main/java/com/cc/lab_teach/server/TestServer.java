package com.cc.lab_teach.server;

import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.mapper.Test;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestServer {
    @Resource
    private Test test;

    public List<Student> getStudents() {
        return test.selectAllStudent();
    }
}
