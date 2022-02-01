package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Answer;
import com.cc.lab_teach.domain.Teacher;
import com.cc.lab_teach.domain.TeacherExample;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.TeacherMapper;
import com.cc.lab_teach.req.AnswerReq;
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

    @Resource
    private AnswerService answerService;

    public TeacherResp login(TeacherReq teacher) {
        Teacher st = teacherMapper.selectByPrimaryKey(teacher.getId());// 根据教工号查询
        if(!ObjectUtils.isEmpty(st)) { // 查询到该教师
            if (st.getPassword().equals(teacher.getPassword())) { // 密码正确
                TeacherResp resp = CopyUtil.copy(st, TeacherResp.class);
                String token = UUID.randomUUID().toString();
                resp.setToken(token);
                LOG.info("返回: {}", resp);
                return resp;
            }
        }
        throw new BusinessException(BusinessExceptionCode.LOGIN_TEACHER_ERROR);
    }

    public void resetPassword(TeacherReq teacher) {
        Teacher t = teacherMapper.selectByPrimaryKey(teacher.getId());
        // 参数校验
        if (!ObjectUtils.isEmpty(t)) {
            if (t.getName().equals(teacher.getName())) { // 参数校验成功, 开始修改密码
                Teacher copy = CopyUtil.copy(teacher, Teacher.class);
                int i = teacherMapper.updateByPrimaryKey(copy);
                LOG.info("已修改 {} 例 ", i);
                if (i == 1) {
                    return;
                }
            }
        }
        // 该教师不存在
        throw new BusinessException(BusinessExceptionCode.RESET_PASSWORD_ERROR);
    }

    public boolean hasObject(String id) {
        LOG.info("开始查询教工号为: {} 的教师是否存在", id);
        Teacher teacher = teacherMapper.selectByPrimaryKey(id);
        // 如果 teacher 为空, 则代表者: 该 teacher 不存在, 返回 false
        return !ObjectUtils.isEmpty(teacher);
    }

    public void putCorrect(AnswerReq answer) {
        if (ObjectUtils.isEmpty(answer.getCorrect())) throw new BusinessException(BusinessExceptionCode.CORRECT_IS_EMPTY);
        answerService.putCorrect(CopyUtil.copy(answer, Answer.class));
    }
}
