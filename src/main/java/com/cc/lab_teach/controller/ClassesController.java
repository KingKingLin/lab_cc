package com.cc.lab_teach.controller;

import com.cc.lab_teach.req.PageReq;
import com.cc.lab_teach.resp.ClassesResp;
import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.resp.PageResp;
import com.cc.lab_teach.service.ClassesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class ClassesController {
    private static final Logger LOG = LoggerFactory.getLogger(ClassesController.class);

    @Resource
    private ClassesService classesService;

    /**
     * @param id 教工号
     * @param name 需要创建的班级名
     * @return 是否创建成功等信息
     */
    @GetMapping("/add-class")
    public CommonResp<Boolean> addClasses(String id, String name) {
        Boolean result = classesService.addClasses(id, name);
        CommonResp<Boolean> resp = new CommonResp<>();
        resp.setContent(result);
        resp.setMessage("创建班级 【" + name + "】 成功");
        return resp;
    }

    /**
     * 根据教工号, 查询该教师管理的所有班级
     * @param id 教工号
     * @return 班级集合
     */
    @GetMapping("/all-classes")
    public CommonResp<List<ClassesResp>> getAllClasses(String id) {
        List<ClassesResp> classes = classesService.getAllClasses(id);
        CommonResp<List<ClassesResp>> resp = new CommonResp<>();
        resp.setContent(classes);
        resp.setMessage("查询班级数据成功");
        return resp;
    }

    /**
     * 根据教工号, 查询该教师管理的部分班级, 请求路径: '/teacher/part-classes?id=xxx&page=xxx&size=xxx
     * @param id 教工号
     * @return 班级集合
     */
    @GetMapping("/part-classes")
    public CommonResp<PageResp<ClassesResp>> getPartClasses(String id, PageReq page) {
        PageResp<ClassesResp> classes = classesService.getPartClasses(id, page);
        CommonResp<PageResp<ClassesResp>> resp = new CommonResp<>();
        resp.setContent(classes);
        resp.setMessage("查询班级数据成功");
        return resp;
    }
}
