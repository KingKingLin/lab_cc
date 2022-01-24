package com.cc.lab_teach.mapper;

import com.cc.lab_teach.domain.CS;
import com.cc.lab_teach.domain.CSExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CSMapper {
    long countByExample(CSExample example);

    int deleteByExample(CSExample example);

    int deleteByPrimaryKey(@Param("cId") Integer cId, @Param("sId") String sId);

    int insert(CS record);

    int insertSelective(CS record);

    List<CS> selectByExample(CSExample example);

    int updateByExampleSelective(@Param("record") CS record, @Param("example") CSExample example);

    int updateByExample(@Param("record") CS record, @Param("example") CSExample example);
}