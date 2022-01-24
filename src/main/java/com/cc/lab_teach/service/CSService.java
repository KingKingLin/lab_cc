package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.CS;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.CSMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CSService {
    private static final Logger LOG = LoggerFactory.getLogger(CSService.class);

    @Resource
    private CSMapper csMapper;

    public void addCS(int c_id, String s_id) {
        CS cs = new CS();
        cs.setcId(c_id); // classes 的 id
        cs.setsId(s_id); // student 的 id
        LOG.info("正在添加一条记录: {}", cs);
        try {
            csMapper.insert(cs);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionCode.REPEAT_TO_ADD);
        }
    }
}
