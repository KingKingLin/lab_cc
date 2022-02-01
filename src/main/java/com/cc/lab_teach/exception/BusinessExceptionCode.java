package com.cc.lab_teach.exception;

public enum BusinessExceptionCode {

    Not_EXITS("此学号不存在, 请联系相关教师"),
    LOGIN_STUDENT_ERROR("密码错误"),
    LOGIN_TEACHER_ERROR("教工号不存在或密码错误"),
    RESET_PASSWORD_ERROR("修改密码失败"),
    DANGEROUS_OPERATION("非法操作"),
    REPEAT_TO_ADD("重复添加"),
    FileIsEmpty("上传空文件"),
    STUDENT_HAS_NOT_SUBMIT("学生并未提交回答，无法评阅"),
    CORRECT_IS_EMPTY("评阅为空"),
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
