package com.cc.lab_teach.mapper;

import com.cc.lab_teach.domain.Classes;
import com.cc.lab_teach.domain.Student;

import java.util.List;

public interface MyMapper {
    List<Classes> getAllClasses(String id);

    int insert(Student student);

    List<Student> selectByCid(int c_id);
}
