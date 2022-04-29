package com.yisen.hbaseboot.util.exception;

public class DaoException extends Exception {

    private String message;
    private int code = 400;

    public DaoException(String message) {
        super(message);
        this.message = message;
    }

    public DaoException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
