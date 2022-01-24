package com.cc.lab_teach.resp;

import java.util.List;

public class PageResp<T> {
    private long total;

    private long size;

    private List<T> list;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", size=" + size +
                ", list=" + list +
                '}';
    }
}