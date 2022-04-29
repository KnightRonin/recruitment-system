package com.yisen.hbaseboot.controller;

import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.exception.ControllerException;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @AuthorList: LiuYiSen, LuWei
 * @Date: 2020/6/5 10:36
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //  全局异常处理类
    @ExceptionHandler(DaoException.class)
    public ResultData daoException(HttpServletRequest req, DaoException e) {
        e.printStackTrace();
        return new ResultData(false, e.getMessage(), e.getCode() == 0 ? 400 : e.getCode(), null);
    }

    @ExceptionHandler(ControllerException.class)
    public ResultData controllerException(HttpServletRequest req, ControllerException e) {
        return new ResultData(false, "请求失败:" + e.getMessage(), 400, null);
    }

    @ExceptionHandler(ServiceException.class)
    public ResultData serviceException(HttpServletRequest req, ServiceException e) {

        return new ResultData(false, e.getMessage(), e.getCode() == 0 ? 400 : e.getCode(), null);
    }
}
