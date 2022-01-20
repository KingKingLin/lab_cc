package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.StudentMapper;
import com.cc.lab_teach.req.StudentReq;
import com.cc.lab_teach.resp.StudentResp;
import com.cc.lab_teach.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class StudentService {
    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    @Resource
    private StudentMapper studentMapper;

    public StudentResp login(StudentReq student) {
        Student st = studentMapper.selectByPrimaryKey(student.getId());// 根据学号查询
        if(!ObjectUtils.isEmpty(st)) { // 查询到该学生
            if (st.getPassword().equals(student.getPassword())) { // 密码正确
                StudentResp resp = CopyUtil.copy(st, StudentResp.class);
                String token = UUID.randomUUID().toString();
                resp.setToken(token);
                LOG.info("返回: {}", resp);
                return resp;
            } else {
                throw new BusinessException(BusinessExceptionCode.LOGIN_STUDENT_ERROR);
            }
        }
        throw new BusinessException(BusinessExceptionCode.Not_EXITS);
    }
}
