package com.example.domain;

import java.io.Serializable;

/**
 * @类名 Remark
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/30 19:49
 * @版本 1.0
 */
public class Remark implements Serializable {

    private static final long serialVersionUID = -3713198158255473163L;

    private String rid;

    private String content;

    private String pid;

    private String remarkBy;

    private String remarkTime;

    private Double score;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRemarkBy() {
        return remarkBy;
    }

    public void setRemarkBy(String remarkBy) {
        this.remarkBy = remarkBy;
    }

    public String getRemarkTime() {
        return remarkTime;
    }

    public void setRemarkTime(String remarkTime) {
        this.remarkTime = remarkTime;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
