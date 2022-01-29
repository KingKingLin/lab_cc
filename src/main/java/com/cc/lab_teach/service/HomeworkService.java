package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Homework;
import com.cc.lab_teach.mapper.HomeworkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HomeworkService {
    private final static Logger LOG = LoggerFactory.getLogger(HomeworkService.class);

    @Resource
    private HomeworkMapper homeworkMapper;

    /**
     * 发布题目
     */
    public void insertHomework(long e_id, String type, String content) {
        LOG.info("开始为【实验 {}】创建题目, 类型为: {}", e_id, type);
        Homework homework = new Homework();
        homework.seteId(e_id);
        homework.setContentType(type);
        if (type.equals("html")) homework.setContent(content);
        else homework.setContent(type+ "/" + content);
        homeworkMapper.insert(homework);
    }
}
