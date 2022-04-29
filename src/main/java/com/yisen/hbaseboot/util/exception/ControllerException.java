package com.yisen.hbaseboot.util.exception;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/5 11:27
 */
public class ControllerException extends Exception {
    private String message;

    public ControllerException(String message) {
        super(message);
        this.message = message;
    }
}
