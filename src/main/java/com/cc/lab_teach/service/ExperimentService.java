package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Experiment;
import com.cc.lab_teach.domain.ExperimentExample;
import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.mapper.ExperimentMapper;
import com.cc.lab_teach.mapper.MyMapper;
import com.cc.lab_teach.req.PageReq;
import com.cc.lab_teach.resp.ExperimentResp;
import com.cc.lab_teach.resp.PageResp;
import com.cc.lab_teach.resp.StudentDetailResp;
import com.cc.lab_teach.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    @Resource
    private MyMapper myMapper;

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

    @Transactional
    public PageResp<StudentDetailResp> getDetails(int c_id, long e_id, PageReq page) {
        LOG.info("开始查询~~");
        // 分页查询
        PageHelper.startPage(page.getPage(), page.getSize());

        List<Student> students = myMapper.selectByCid(c_id);
        List<StudentDetailResp> details = CopyUtil.copyList(students, StudentDetailResp.class);

        int total = myMapper.getTotal(e_id);
        for (StudentDetailResp detail : details) {
            String id = detail.getId();
            detail.setCorrects(myMapper.getCorrects(e_id, id));
            detail.setResults(myMapper.getResults(e_id, id));
            detail.setTotal(total);
        }
        PageInfo<Student> pageInfo = new PageInfo<>(students);
        LOG.info("总页数: {}", pageInfo.getPages());
        LOG.info("总条目: {}", pageInfo.getTotal());

        PageResp<StudentDetailResp> results = new PageResp<>();
        results.setList(details);
        results.setTotal(pageInfo.getTotal());
        results.setSize(pageInfo.getPages());
        return results;
    }
}
