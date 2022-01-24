package com.cc.lab_teach.domain;

public class CS {
    private Integer cId;

    private String sId;

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", cId=").append(cId);
        sb.append(", sId=").append(sId);
        sb.append("]");
        return sb.toString();
    }
}