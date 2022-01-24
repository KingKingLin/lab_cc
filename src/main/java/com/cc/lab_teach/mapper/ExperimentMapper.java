package com.cc.lab_teach.mapper;

import com.cc.lab_teach.domain.Experiment;
import com.cc.lab_teach.domain.ExperimentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExperimentMapper {
    long countByExample(ExperimentExample example);

    int deleteByExample(ExperimentExample example);

    int deleteByPrimaryKey(Long eId);

    int insert(Experiment record);

    int insertSelective(Experiment record);

    List<Experiment> selectByExample(ExperimentExample example);

    Experiment selectByPrimaryKey(Long eId);

    int updateByExampleSelective(@Param("record") Experiment record, @Param("example") ExperimentExample example);

    int updateByExample(@Param("record") Experiment record, @Param("example") ExperimentExample example);

    int updateByPrimaryKeySelective(Experiment record);

    int updateByPrimaryKey(Experiment record);
}