package com.example.exception;
/*
 *自定义异常
 */
public class CustomerException extends RuntimeException {
    private String msg;
    private String code;

    public CustomerException(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }
    public CustomerException(String msg) {
        this.msg = msg;
        this.code = "500";
    }
    public CustomerException() {
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
