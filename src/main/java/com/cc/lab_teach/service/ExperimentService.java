package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Experiment;
import com.cc.lab_teach.domain.ExperimentExample;
import com.cc.lab_teach.mapper.ExperimentMapper;
import com.cc.lab_teach.resp.ExperimentResp;
import com.cc.lab_teach.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ExperimentService {
    private static final Logger LOG = LoggerFactory.getLogger(ExperimentService.class);

    @Resource
    private ExperimentMapper experimentMapper;

    public List<ExperimentResp> getAllExperiments(int c_id) {
        // 查询条件
        ExperimentExample example = new ExperimentExample();
        ExperimentExample.Criteria criteria = example.createCriteria();
        criteria.andCIdEqualTo(c_id); // where c_id = c_id

        List<Experiment> list = experimentMapper.selectByExample(example);
        List<ExperimentResp> result = CopyUtil.copyList(list, ExperimentResp.class);
        return result;
    }

    @Transactional
    public int addExperiment(int c_id, String title, Date deadline) {
        Experiment experiment = new Experiment();
        experiment.setcId(c_id);
        experiment.setTitle(title);
        experiment.setDeadline(deadline);
        return experimentMapper.insert(experiment);
    }
}
