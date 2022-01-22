package com.cc.lab_teach.mapper;

import com.cc.lab_teach.domain.Classes;

import java.util.List;

public interface MyMapper {
    List<Classes> getAllClasses(String id);
}
