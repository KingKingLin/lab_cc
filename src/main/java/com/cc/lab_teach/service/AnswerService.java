package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Answer;
import com.cc.lab_teach.domain.AnswerExample;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.AnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AnswerService {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerService.class);

    @Resource
    private AnswerMapper answerMapper;

    public void putAnswer(Answer answer) {
        // 先开始查询该学生该题目是否提交过
        LOG.info("开始判断该学生 {} 是否提交过答案", answer.getsId());
        // and s_id = xx and h_id = xx 得到一条记录，如果为空则代表没有插入过，如果不为空则判断是否可以重做
        AnswerExample answerExample = new AnswerExample();
        AnswerExample.Criteria criteria = answerExample.createCriteria();
        criteria.andSIdEqualTo(answer.getsId());
        criteria.andHIdEqualTo(answer.gethId());
        // 得到查询结果
        List<Answer> answers = answerMapper.selectByExample(answerExample);
        // 判断是否为空
        if (ObjectUtils.isEmpty(answers) || answers.get(0).getRedo()) { // 如果为空，则代表没创建过
            answer.setRedo(false);
            answerMapper.insert(answer);
        }
    }

    public void putCorrect(Answer answer) {
        // 先开始查询该学生该题目是否提交过
        LOG.info("开始判断该学生 {} 是否提交过答案", answer.getsId());
        // and s_id = xx and h_id = xx 得到一条记录，如果为空则代表没有插入过，如果不为空则判断是否可以重做
        AnswerExample answerExample = new AnswerExample();
        AnswerExample.Criteria criteria = answerExample.createCriteria();
        criteria.andSIdEqualTo(answer.getsId());
        criteria.andHIdEqualTo(answer.gethId());
        // 得到查询结果
        List<Answer> answers = answerMapper.selectByExampleWithBLOBs(answerExample);
        // 判断是否为空
        if (ObjectUtils.isEmpty(answers)) { // 如果为空，则代表没创建过
            throw new BusinessException(BusinessExceptionCode.STUDENT_HAS_NOT_SUBMIT);
        } else {
            LOG.info("提交评阅中...");
            Answer result = answers.get(0);
            result.setCorrect(answer.getCorrect());
            answerMapper.updateByPrimaryKeyWithBLOBs(result);
        }
    }
}
