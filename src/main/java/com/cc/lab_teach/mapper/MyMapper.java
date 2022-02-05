package com.cc.lab_teach.mapper;

import com.cc.lab_teach.domain.Classes;
import com.cc.lab_teach.domain.Experiment;
import com.cc.lab_teach.domain.Snapshot;
import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.resp.StudentHomeworkResp;

import java.util.List;

public interface MyMapper {
    List<Classes> getAllClasses(String id);

    int insert(Student student);

    List<Student> selectByCid(int c_id);

    int getCorrects(long e_id, String s_id);

    int getResults(long e_id, String s_id);

    int getTotal(long e_id);

    List<Experiment> getExperiments(String id);

    List<StudentHomeworkResp> getStudentHomework(long e_id, String s_id);

    void insertSnapshot(long views);

    List<Snapshot> get30DayStatistic();

    // 删除与 c_id 班级信息相关的数据
    void deleteClass(int c_id);
}
