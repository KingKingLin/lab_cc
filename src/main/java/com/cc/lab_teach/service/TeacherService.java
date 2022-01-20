package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Teacher;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.TeacherMapper;
import com.cc.lab_teach.req.TeacherReq;
import com.cc.lab_teach.resp.TeacherResp;
import com.cc.lab_teach.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class TeacherService {
    private static final Logger LOG = LoggerFactory.getLogger(TeacherService.class);

    @Resource
    private TeacherMapper teacherMapper;

    public TeacherResp login(TeacherReq teacher) {
        Teacher st = teacherMapper.selectByPrimaryKey(teacher.getId());// 根据教工号查询
        if(!ObjectUtils.isEmpty(st)) { // 查询到该教师
            if (st.getPassword().equals(teacher.getPassword())) { // 密码正确
                TeacherResp resp = CopyUtil.copy(st, TeacherResp.class);
                String token = UUID.randomUUID().toString();
                resp.setToken(token);
                LOG.info("返回: {}", resp);
                return resp;
            } else {
                throw new BusinessException(BusinessExceptionCode.LOGIN_STUDENT_ERROR);
            }
        }
        throw new BusinessException(BusinessExceptionCode.LOGIN_STUDENT_ERROR);
    }
}
