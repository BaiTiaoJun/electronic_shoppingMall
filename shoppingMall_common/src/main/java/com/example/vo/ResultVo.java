package com.example.vo;

import java.io.Serializable;

/**
 * @类名 ResultVo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/1 10:06
 * @版本 1.0
 */
public class ResultVo implements Serializable {
    private static final long serialVersionUID = -1787767729572315293L;
    private Boolean flag;
    private String message;
    private String fileName;

    public ResultVo() {
    }

    public ResultVo(Boolean flag) {
        this.flag = flag;
    }

    public ResultVo(Boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public ResultVo(Boolean flag, String message, String fileName) {
        this.flag = flag;
        this.message = message;
        this.fileName = fileName;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
