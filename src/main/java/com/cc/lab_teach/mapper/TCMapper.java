package com.cc.lab_teach.mapper;

import com.cc.lab_teach.domain.TC;
import com.cc.lab_teach.domain.TCExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TCMapper {
    long countByExample(TCExample example);

    int deleteByExample(TCExample example);

    int deleteByPrimaryKey(@Param("tId") String tId, @Param("cId") Integer cId);

    int insert(TC record);

    int insertSelective(TC record);

    List<TC> selectByExample(TCExample example);

    int updateByExampleSelective(@Param("record") TC record, @Param("example") TCExample example);

    int updateByExample(@Param("record") TC record, @Param("example") TCExample example);
}