package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.MyMapper;
import com.cc.lab_teach.mapper.StudentMapper;
import com.cc.lab_teach.req.AddStudentReq;
import com.cc.lab_teach.req.StudentReq;
import com.cc.lab_teach.resp.AllStudent;
import com.cc.lab_teach.resp.StudentResp;
import com.cc.lab_teach.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private MyMapper myMapper;

    @Resource
    private CSService csService;

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

    @Transactional // 开启事务
    public void addStudent(AddStudentReq student, int c_id) {
        Student stu = CopyUtil.copy(student, Student.class);
        // 为 student 表赋值
        myMapper.insert(stu);
        // 为 c_s 表创建 班级 和 学生的关系
        csService.addCS(c_id, stu.getId());
    }

    public List<AllStudent> getAllStudents(int c_id) {
        List<Student> lists = myMapper.selectByCid(c_id);
        List<AllStudent> students = CopyUtil.copyList(lists, AllStudent.class);
        return students;
    }
}
