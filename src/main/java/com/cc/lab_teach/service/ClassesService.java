package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.Classes;
import com.cc.lab_teach.domain.ClassesExample;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.ClassesMapper;
import com.cc.lab_teach.mapper.MyMapper;
import com.cc.lab_teach.mapper.TCMapper;
import com.cc.lab_teach.resp.ClassesResp;
import com.cc.lab_teach.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理 classes 和 t_c 表的 service 层
 */
@Service
public class ClassesService {
    private static final Logger LOG = LoggerFactory.getLogger(ClassesService.class);

    @Resource
    private ClassesMapper classesMapper; // 操作 classes 表

    @Resource
    private TCService tcService; // 关联 tcService 对象 ( 职责隔离原则 )

    @Resource
    private TeacherService teacherService; // 关联 teacherService 对象 ( 职责隔离原则 )

    @Resource
    private MyMapper myMapper; // 自定义 Mapper 实例

    public Boolean addClasses(String id, String name) {
        // 1. 先根据 id 查询, 是否该教师存在, 如果不存在则抛出异常
        if (!teacherService.hasObject(id)) {
            LOG.warn("教工号为: {} 的教师不存在", id);
            throw new BusinessException(BusinessExceptionCode.DANGEROUS_OPERATION);
        }
        // 2. 再判断该班级是否已经被创建过, 不可以重复创建, 如果已经创建则, 取出其 c_id
        if (this.hasCreatedClasses(name)) {
            LOG.info("班级名为: {} 已经被创建过", name);
        } else {
            // 3. 以上条件都满足后, 在 classes 表中插入一条 值为 name 的数据
            LOG.info("开始创建班级 {}", name);
            Classes classes = new Classes();
            classes.setName(name);
            classesMapper.insert(classes);
        }
        // 4. 更新 t_C 教师-班级关系表
        tcService.addTC(id, this.getID(name));
        // 5. 返回 创建成功的信息
        return true;
    }

    private int getID(String name) {
        ClassesExample classesExample = new ClassesExample();
        ClassesExample.Criteria criteria = classesExample.createCriteria();
        criteria.andNameEqualTo(name); // name = name

        List<Classes> classes = classesMapper.selectByExample(classesExample);
        return classes.get(0).getId();
    }

    // 查询该班级是否被创建过
    private boolean hasCreatedClasses(String name) {
        LOG.info("开始查询班级名为: {} 是否被创建过...", name);

        // 查询条件
        ClassesExample classesExample = new ClassesExample();
        ClassesExample.Criteria criteria = classesExample.createCriteria();
        criteria.andNameEqualTo(name); // and name == name

        List<Classes> classes = classesMapper.selectByExample(classesExample);
        LOG.info("班级创建的结果: {}", !ObjectUtils.isEmpty(classes));
        return !ObjectUtils.isEmpty(classes);
    }

    public List<ClassesResp> getAllClasses(String id) {
        LOG.info("教工号为: {} 请求查询跟其有关的所有班级信息", id);
        List<Classes> list = myMapper.getAllClasses(id);
        List<ClassesResp> resps = CopyUtil.copyList(list, ClassesResp.class);
        return resps;
    }
}
