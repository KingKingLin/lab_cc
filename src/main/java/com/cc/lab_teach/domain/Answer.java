package com.cc.lab_teach.domain;

public class Answer {
    private Long aId;

    private String sId;

    private Long hId;

    private Boolean redo;

    private String correct;

    private String result;

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public Long gethId() {
        return hId;
    }

    public void sethId(Long hId) {
        this.hId = hId;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", aId=").append(aId);
        sb.append(", sId=").append(sId);
        sb.append(", hId=").append(hId);
        sb.append(", redo=").append(redo);
        sb.append(", correct=").append(correct);
        sb.append(", result=").append(result);
        sb.append("]");
        return sb.toString();
    }
}