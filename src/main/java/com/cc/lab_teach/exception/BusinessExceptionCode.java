package com.cc.lab_teach.exception;

public enum BusinessExceptionCode {

    Not_EXITS("此学号不存在, 请联系相关教师"),
    LOGIN_STUDENT_ERROR("密码错误"),
    LOGIN_TEACHER_ERROR("教工号不存在或密码错误"),
    RESET_PASSWORD_ERROR("修改密码失败"),
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
