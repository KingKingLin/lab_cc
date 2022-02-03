//package com.cc.lab_teach.controller;
//
//import com.cc.lab_teach.exception.BusinessException;
//import com.cc.lab_teach.exception.BusinessExceptionCode;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.ResourceUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//
//@Controller
//public class VideoController {
//    private static final Logger LOG = LoggerFactory.getLogger(VideoController.class);
//
//    @GetMapping("/video/{filename}")
//    public void getStreamData(@PathVariable String filename, HttpServletResponse response) throws IOException {
//        LOG.info("请求读取 {} 文件", filename);
//        if (!filename.endsWith(".mp4")) throw new BusinessException(BusinessExceptionCode.NOT_MP4_FILE);
//        File file = new File(ResourceUtils.getURL("classpath:").getPath() + "static/video/" + filename);
//        if (!file.exists()) throw new BusinessException(BusinessExceptionCode.FILE_IS_NOT_EXITS); // 前端需要请求的视频文件不存在
//        // 获取后端需要读取的文件
//        FileInputStream fis = new FileInputStream(file);
//        // 缓冲流封装输入流
//        BufferedInputStream bis = new BufferedInputStream(fis);
//        byte[] bytes = new byte[1024 * 10];
//        // 获取传给前端的流
//        OutputStream os = response.getOutputStream(); // 输出给前端
//        BufferedOutputStream bos = new BufferedOutputStream(os);
//
//        // 开始给前端传递视频文件
//        int i = 0;
//        while ((i = bis.read(bytes)) != -1) {
//            bos.write(bytes, 0, i);
//        }
//
//        bis.close(); // 关闭输入流
//        bos.close(); // 关闭输出流
//    }
//}
