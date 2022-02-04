package com.cc.lab_teach.resp;

public class HomeworkResp {
    private Long hId;

    private String contentType;

    private String standardType;

    private String content;

    private String standard;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", hId=").append(hId);
        sb.append(", contentType=").append(contentType);
        sb.append(", standardType=").append(standardType);
        sb.append(", content=").append(content);
        sb.append(", standard=").append(standard);
        sb.append("]");
        return sb.toString();
    }
}