package com.cc.lab_teach.mapper;

import com.cc.lab_teach.domain.Homework;
import com.cc.lab_teach.domain.HomeworkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HomeworkMapper {
    long countByExample(HomeworkExample example);

    int deleteByExample(HomeworkExample example);

    int deleteByPrimaryKey(Long hId);

    int insert(Homework record);

    int insertSelective(Homework record);

    List<Homework> selectByExampleWithBLOBs(HomeworkExample example);

    List<Homework> selectByExample(HomeworkExample example);

    Homework selectByPrimaryKey(Long hId);

    int updateByExampleSelective(@Param("record") Homework record, @Param("example") HomeworkExample example);

    int updateByExampleWithBLOBs(@Param("record") Homework record, @Param("example") HomeworkExample example);

    int updateByExample(@Param("record") Homework record, @Param("example") HomeworkExample example);

    int updateByPrimaryKeySelective(Homework record);

    int updateByPrimaryKeyWithBLOBs(Homework record);

    int updateByPrimaryKey(Homework record);
}