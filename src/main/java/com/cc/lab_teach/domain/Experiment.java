package com.cc.lab_teach.domain;

import java.util.Date;

public class Experiment {
    private Long eId;

    private Integer cId;

    private String title;

    private Date deadline;

    public Long geteId() {
        return eId;
    }

    public void seteId(Long eId) {
        this.eId = eId;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", eId=").append(eId);
        sb.append(", cId=").append(cId);
        sb.append(", title=").append(title);
        sb.append(", deadline=").append(deadline);
        sb.append("]");
        return sb.toString();
    }
}