package com.cc.lab_teach.controller;

import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.resp.CommonResp;
import com.cc.lab_teach.resp.HomeworkResp;
import com.cc.lab_teach.service.HomeworkService;
import com.cc.lab_teach.util.OfficeConvertUtil;
import com.mysql.cj.log.Log;
import com.sun.java.accessibility.util.java.awt.ListTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 处理发布题目的类
 */
@RestController
@RequestMapping("/teacher")
public class HomeworkController {
    private final static Logger LOG = LoggerFactory.getLogger(HomeworkController.class);

    private final static HashMap<String, String> hashMap = new HashMap<>();

    @Resource
    private HomeworkService homeworkService;

    static {
        try {
            // 获得 classpath 的绝对路径
            hashMap.put("image", ResourceUtils.getURL("classpath:").getPath() + "static/image");
            hashMap.put("video", ResourceUtils.getURL("classpath:").getPath() + "static/video");
        } catch (FileNotFoundException e) {
            LOG.error(String.valueOf(e));
        }
    }

    /**
     * homework 可能是图片，可能是文本(word，由前端解析过)，还可能是视频
     * 1. 如果是 图片，则保存到本地，记录其存储路径，最后将路径存入数据库
     * 2. 如果是 文本，则直接存入数据库
     * 3. 如果是 视频，则和图片一样的处理
     * @return
     */
    @PostMapping("/release")
    public CommonResp<Boolean> release(long h_id, MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException(BusinessExceptionCode.FILE_IS_EMPTY);

        LOG.info("开始上传答案");
        LOG.info("h_id: " + h_id);
        LOG.info("file: " + file.getOriginalFilename());
        LOG.info("contentType" + file.getContentType());
        if (file.getOriginalFilename().endsWith(".doc")) { // 处理 doc 文件
            LOG.info("开始处理 .doc 文件");
            String htmlContent = OfficeConvertUtil.docToHtml(file);
            LOG.info("处理 .doc 文件 完成");
            homeworkService.uploadHomework(h_id, "html", htmlContent);
        } else if (file.getOriginalFilename().endsWith(".docx")) { // 处理 docx 文件
            LOG.info("开始处理 .doc 文件");
            String htmlContent = OfficeConvertUtil.docxToHtml(file);
            homeworkService.uploadHomework(h_id, "html", htmlContent);
            LOG.info("处理 .doc 文件 完成");
        } else if (file.getOriginalFilename().endsWith(".txt")) { // 处理 txt 文件
            LOG.info("开始处理 .txt 文件");
            String txtContent = handleTxt(file);
            homeworkService.uploadHomework(h_id, "txt", txtContent);
            LOG.info("处理 .txt 文件 完成");
        } else {
            LOG.info("开始处理 媒体 文件");
            String[] handle = handleImageAndVideo(file);
            homeworkService.uploadHomework(h_id, handle[0], handle[0] + "/" + handle[1]);
            LOG.info("处理 媒体 文件完成");
        }

        CommonResp<Boolean> resp = new CommonResp<>();
        resp.setMessage("上传成功");
        resp.setContent(true);
        return resp;
    }

    @GetMapping("/get-homeworks")
    public CommonResp<List<HomeworkResp>> getHomework(long e_id) {
        LOG.info("开始查询 {} 的所有题目", e_id);
        List<HomeworkResp> result = homeworkService.getHomework(e_id);
        CommonResp<List<HomeworkResp>> resp = new CommonResp<>();
        resp.setContent(result);
        resp.setMessage("查询成功");
        return resp;
    }

    /**
     * homework 可能是图片，可能是文本(word，由前端解析过)，还可能是视频
     * 1. 如果是 图片，则保存到本地，记录其存储路径，最后将路径存入数据库
     * 2. 如果是 文本，则直接存入数据库
     * 3. 如果是 视频，则和图片一样的处理
     * @return
     */
    @PostMapping("/upload")
    public CommonResp<Boolean> upload(long e_id, MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException(BusinessExceptionCode.FILE_IS_EMPTY);

        LOG.info("开始发布题目");
        LOG.info("e_id: " + e_id);
        LOG.info("file: " + file.getOriginalFilename());
        LOG.info("contentType" + file.getContentType());
        if (file.getOriginalFilename().endsWith(".doc")) { // 处理 doc 文件
            LOG.info("开始处理 .doc 文件");
            String htmlContent = OfficeConvertUtil.docToHtml(file);
            LOG.info("处理 .doc 文件 完成");
            homeworkService.insertHomework(e_id, "html", htmlContent);
        } else if (file.getOriginalFilename().endsWith(".docx")) { // 处理 docx 文件
            LOG.info("开始处理 .doc 文件");
            String htmlContent = OfficeConvertUtil.docxToHtml(file);
            homeworkService.insertHomework(e_id, "html", htmlContent);
            LOG.info("处理 .doc 文件 完成");
        } else if (file.getOriginalFilename().endsWith(".txt")) { // 处理 txt 文件
            LOG.info("开始处理 .txt 文件");
            String txtContent = handleTxt(file);
            homeworkService.insertHomework(e_id, "txt", txtContent);
            LOG.info("处理 .txt 文件 完成");
        } else {
            LOG.info("开始处理 媒体 文件");
            String[] handle = handleImageAndVideo(file);
            homeworkService.insertHomework(e_id, handle[0], handle[0] + "/" + handle[1]);
            LOG.info("处理 媒体 文件完成");
        }
        CommonResp<Boolean> resp = new CommonResp<>();
        resp.setMessage("上传成功");
        resp.setContent(true);
        return resp;
    }

    private String handleTxt(MultipartFile file) throws IOException {
        Reader reader = new InputStreamReader(file.getInputStream());
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[1024];
        int i = 0;
        while ((i = reader.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, i));
        }
        return stringBuilder.toString();
    }

    private String[] handleImageAndVideo(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new BusinessException(BusinessExceptionCode.FILE_IS_EMPTY);
        // 获取文件名
        String realPath = null;
        String type = null;
        String contentType = file.getContentType();
        if (contentType.contains("image")) { // 如果是图片
            realPath = hashMap.get("image");
            type = "image";
        } else if (contentType.contains("video")) { // 如果是视频
            realPath = hashMap.get("video");
            type = "video";
        }
        File newFile = new File(realPath);
        // 如果文件夹不存在、则新建
        if (!newFile.exists()) newFile.mkdirs();
        // 上传，不使用用户上传的名字，因为容易重复
        String name = UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        file.transferTo(new File(newFile, name));

        return new String[]{type, name};
    }
}
