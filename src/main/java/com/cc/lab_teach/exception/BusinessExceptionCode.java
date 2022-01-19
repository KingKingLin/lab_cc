package com.cc.lab_teach.exception;

public enum BusinessExceptionCode {

    LOGIN_STUDENT_ERROR("学号不存在或密码错误"),
    LOGIN_TEACHER_ERROR("教工号不存在或密码错误"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
