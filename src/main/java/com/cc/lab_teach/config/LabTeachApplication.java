package com.cc.lab_teach.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.cc.lab_teach")
public class LabTeachApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabTeachApplication.class, args);
    }

}
