package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Homework;
import com.cc.lab_teach.domain.HomeworkExample;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.HomeworkMapper;
import com.cc.lab_teach.resp.HomeworkResp;
import com.cc.lab_teach.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HomeworkService {
    private final static Logger LOG = LoggerFactory.getLogger(HomeworkService.class);

    @Resource
    private HomeworkMapper homeworkMapper;

    /**
     * 发布题目
     */
    @Transactional
    public void insertHomework(long e_id, String type, String content) {
        LOG.info("开始为【实验 {}】创建题目, 类型为: {}", e_id, type);
        Homework homework = new Homework();
        homework.seteId(e_id);
        homework.setContentType(type);
        homework.setContent(content);
        homeworkMapper.insert(homework);
    }

    public List<HomeworkResp>  getHomework(long e_id) {
        // and e_id = #{e_id}
        HomeworkExample homeworkExample = new HomeworkExample();
        HomeworkExample.Criteria criteria = homeworkExample.createCriteria();
        criteria.andEIdEqualTo(e_id);

        List<Homework> homeworks = homeworkMapper.selectByExampleWithBLOBs(homeworkExample);
        return CopyUtil.copyList(homeworks, HomeworkResp.class);
    }

    /**
     * 上传答案
     */
    @Transactional
    public void uploadHomework(long h_id, String type, String standard) {
        LOG.info("开始为【题目 {}】上传答案, 类型为: {}", h_id, type);
        Homework homework = homeworkMapper.selectByPrimaryKey(h_id);
        if (ObjectUtils.isEmpty(homework)) throw new BusinessException(BusinessExceptionCode.HOMEWORK_IS_EMPTY); // 给不存在的题目更新
        homework.setStandard(standard);
        homework.setStandardType(type);
        homeworkMapper.updateByPrimaryKeyWithBLOBs(homework);
    }
}
