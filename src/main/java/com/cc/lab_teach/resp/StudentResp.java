package com.cc.lab_teach.resp;

import javax.validation.constraints.Pattern;

public class StudentResp {
    private String token; // token

    private String id;

    private String name;

    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$", message = "【密码】至少包含 数字和英文，长度6-18")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private int type = 0; // 0 表示学生

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
        sb.append(", password=").append(password);
        sb.append(", type=").append(type);
        sb.append(", token=").append(token);
        sb.append("]");
        return sb.toString();
    }
}