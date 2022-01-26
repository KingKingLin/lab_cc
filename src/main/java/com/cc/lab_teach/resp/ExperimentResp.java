package com.cc.lab_teach.resp;

import java.util.Date;

public class ExperimentResp {
    private Long eId;

    private String title;

    private Date deadline;

    public Long geteId() {
        return eId;
    }

    public void seteId(Long eId) {
        this.eId = eId;
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
        sb.append(", title=").append(title);
        sb.append(", deadline=").append(deadline);
        sb.append("]");
        return sb.toString();
    }
}