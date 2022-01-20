package com.cc.lab_teach.resp;

public class TeacherResp {
    private String token; // token

    private String id;

    private String name;

    private int type = 1; // 1 表示教师

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", token=").append(token);
        sb.append("]");
        return sb.toString();
    }
}