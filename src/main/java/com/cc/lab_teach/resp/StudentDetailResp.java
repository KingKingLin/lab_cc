package com.cc.lab_teach.resp;

public class StudentDetailResp {
    private String id;

    private String name;

    private int results;

    private int corrects;

    private int total;

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public int getCorrects() {
        return corrects;
    }

    public void setCorrects(int corrects) {
        this.corrects = corrects;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StudentDetailResp{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", results=" + results +
                ", corrects=" + corrects +
                ", total=" + total +
                '}';
    }
}