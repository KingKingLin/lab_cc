package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Experiment;
import com.cc.lab_teach.domain.Student;
import com.cc.lab_teach.domain.Teacher;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.MyMapper;
import com.cc.lab_teach.mapper.StudentMapper;
import com.cc.lab_teach.req.AddStudentReq;
import com.cc.lab_teach.req.PageReq;
import com.cc.lab_teach.req.StudentReq;
import com.cc.lab_teach.resp.ExperimentResp;
import com.cc.lab_teach.resp.PageResp;
import com.cc.lab_teach.resp.StudentPageResp;
import com.cc.lab_teach.resp.StudentResp;
import com.cc.lab_teach.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
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

    public List<StudentPageResp> getAllStudents(int c_id) {
        List<Student> lists = myMapper.selectByCid(c_id);
        return CopyUtil.copyList(lists, StudentPageResp.class);
    }

    public PageResp<StudentPageResp> getStudentByPage(PageReq page, int c_id) {
        PageHelper.startPage(page.getPage(), page.getSize());
        LOG.info("分页查询班级数据：{}", page);

        List<Student> lists = myMapper.selectByCid(c_id);
        LOG.info("分页查询到的数据: {}", lists);

        PageInfo<Student> pageInfo = new PageInfo<>(lists);
        LOG.info("总行数: {}", pageInfo.getTotal()); // 总行数，一般返回给前端
        LOG.info("总页数: {}", pageInfo.getPages()); // 总页数

        List<StudentPageResp> students = CopyUtil.copyList(lists, StudentPageResp.class);
        PageResp<StudentPageResp> results = new PageResp<>();
        results.setTotal(pageInfo.getTotal());
        results.setSize(pageInfo.getPages());
        results.setList(students);
        return results;
    }

    @Transactional
    public void resetPassword(StudentResp student) {
        Student t = studentMapper.selectByPrimaryKey(student.getId());
        // 参数校验
        if (!ObjectUtils.isEmpty(t)) {
            if (t.getName().equals(student.getName())) { // 参数校验成功, 开始修改密码
                Student copy = CopyUtil.copy(student, Student.class);
                int i = studentMapper.updateByPrimaryKey(copy);
                LOG.info("已修改 {} 例 ", i);
                if (i == 1) {
                    return;
                }
            }
        }
        // 该学生不存在
        throw new BusinessException(BusinessExceptionCode.RESET_PASSWORD_ERROR);
    }

    public List<ExperimentResp> getAllExperiments(String id) {
        List<Experiment> experiments = myMapper.getExperiments(id);
        LOG.info("查询结果: {}", experiments.toString());
        return CopyUtil.copyList(experiments, ExperimentResp.class);
    }
}
