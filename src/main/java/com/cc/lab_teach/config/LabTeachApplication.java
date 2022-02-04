package com.cc.lab_teach.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@ComponentScan("com.cc.lab_teach")
@MapperScan("com.cc.lab_teach.mapper")
@EnableScheduling
//@EnableAsync
public class LabTeachApplication {
    private static final Logger LOG = LoggerFactory.getLogger(LabTeachApplication.class);

    public static void main(String[] args) {
//        SpringApplication.run(WikiApplication.class, args);
        SpringApplication app = new SpringApplication(LabTeachApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功!!");
        LOG.info("地址: \thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }

    /**
     * 配置上传文件大小的配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  设置单个文件大小
        factory.setMaxFileSize(DataSize.parse("300MB"));//KB 或者 MB 都可以 1MB=1024KB。1KB=1024B(字节)
        /// 设置总上传文件大小
        factory.setMaxRequestSize(DataSize.parse("300MB"));//KB 或者 MB 都可以 1MB=1024KB。1KB=1024B(字节)
        return factory.createMultipartConfig();
    }
}
