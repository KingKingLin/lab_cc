package com.cc.lab_teach.domain;

public class Homework {
    private Long hId;

    private Long eId;

    private String content;

    public Long gethId() {
        return hId;
    }

    public void sethId(Long hId) {
        this.hId = hId;
    }

    public Long geteId() {
        return eId;
    }

    public void seteId(Long eId) {
        this.eId = eId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", hId=").append(hId);
        sb.append(", eId=").append(eId);
        sb.append(", content=").append(content);
        sb.append("]");
        return sb.toString();
    }
}