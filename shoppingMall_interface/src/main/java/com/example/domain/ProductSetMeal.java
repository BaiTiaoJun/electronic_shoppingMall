package com.example.domain;

import java.io.Serializable;

public class ProductSetMeal implements Serializable {
    private static final long serialVersionUID = -4378894088159549420L;

    private String sid;

    private String setMeal;

    private String pid;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSetMeal() {
        return setMeal;
    }

    public void setSetMeal(String setMeal) {
        this.setMeal = setMeal;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}