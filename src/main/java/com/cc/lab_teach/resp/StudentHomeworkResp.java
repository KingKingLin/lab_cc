package com.cc.lab_teach.resp;

public class StudentHomeworkResp {
    private Long hId; // 题目 id

    private String contentType;

    private String standardType;

    private String content; // 题目内容

    private String standard; // 标准答案

    private Long aId; // 答案 id

    private Boolean redo;

    private String correct; // 教师的评阅

    private String result; // 学生提交的答案

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    @Override
    public String toString() {
        return "StudentHomeworkResp{" +
                "hId=" + hId +
                ", contentType='" + contentType + '\'' +
                ", standardType='" + standardType + '\'' +
                ", content='" + content + '\'' +
                ", standard='" + standard + '\'' +
                ", aId=" + aId +
                ", redo=" + redo +
                ", correct='" + correct + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

    public Boolean getRedo() {
        return redo;
    }

    public void setRedo(Boolean redo) {
        this.redo = redo;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long gethId() {
        return hId;
    }

    public void sethId(Long hId) {
        this.hId = hId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getStandardType() {
        return standardType;
    }

    public void setStandardType(String standardType) {
        this.standardType = standardType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

}