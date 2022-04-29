package com.yisen.hbaseboot.util;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultData<T> {
    boolean success;
    String msg;
    int code;
    T data;

//    public ResultData(boolean success, String msg, int code, T data) {
//        this.success = success;
//        this.msg = msg;
//        this.code = code;
//        this.data = data;
//    }
}
