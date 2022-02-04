package com.cc.lab_teach.mapper;

import com.cc.lab_teach.domain.Snapshot;
import com.cc.lab_teach.domain.SnapshotExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SnapshotMapper {
    long countByExample(SnapshotExample example);

    int deleteByExample(SnapshotExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Snapshot record);

    int insertSelective(Snapshot record);

    List<Snapshot> selectByExample(SnapshotExample example);

    Snapshot selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Snapshot record, @Param("example") SnapshotExample example);

    int updateByExample(@Param("record") Snapshot record, @Param("example") SnapshotExample example);

    int updateByPrimaryKeySelective(Snapshot record);

    int updateByPrimaryKey(Snapshot record);
}