package com.example.domain;

import java.io.Serializable;

public class ProductColor implements Serializable {
    private static final long serialVersionUID = 4829126430967238448L;

    private String cid;

    private String color;

    private String pid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}