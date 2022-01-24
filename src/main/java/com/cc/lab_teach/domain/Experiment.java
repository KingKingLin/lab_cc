package com.cc.lab_teach.domain;

public class Experiment {
    private Long eId;

    private Integer cId;

    private String title;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", eId=").append(eId);
        sb.append(", cId=").append(cId);
        sb.append(", title=").append(title);
        sb.append("]");
        return sb.toString();
    }
}