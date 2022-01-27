package com.cc.lab_teach.controller;

import com.cc.lab_teach.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class HomeworkController {
    private final static Logger LOG = LoggerFactory.getLogger(HomeworkController.class);

    /**
     * homework 可能是图片，可能是文本(word，由前端解析过)，还可能是视频
     * 1. 如果是 图片，则保存到本地，记录其存储路径，最后将路径存入数据库
     * 2. 如果是 文本，则直接存入数据库
     * 3. 如果是 视频，则和图片一样的处理
     * @return
     */
    @PostMapping("/release-homework")
    public CommonResp releaseHomework() {

        return null;
    }
}
