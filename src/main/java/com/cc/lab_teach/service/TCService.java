package com.cc.lab_teach.service;

import com.cc.lab_teach.domain.TC;
import com.cc.lab_teach.domain.TCExample;
import com.cc.lab_teach.exception.BusinessException;
import com.cc.lab_teach.exception.BusinessExceptionCode;
import com.cc.lab_teach.mapper.TCMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TCService {
    private static final Logger LOG = LoggerFactory.getLogger(TCService.class);

    @Resource
    private TCMapper tcMapper;

    public void addTC(String t_id, int c_id) {
        TC tc = new TC();
        tc.settId(t_id); // teacher 的 id
        tc.setcId(c_id); // classes 的 id
        LOG.info("正在添加一条记录: {}", tc);
        try {
            tcMapper.insert(tc);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionCode.REPEAT_TO_ADD);
        }
    }
}
