package com.yisen.hbaseboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.service.PositionInfoService;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/29 18:53
 */
@RestController
public class PositionInfoController {
    @Autowired
    PositionInfoService positionInfoService;

    @GetMapping("/getPosDetail")
    public ResultData getPosDetail(@RequestParam(value = "pid", defaultValue = "") String pid) throws ServiceException {
//        System.out.println(pid);
        if (pid.equals("") || pid == null) {
            return new ResultData(false, "错误请求", 400, null);
        }
        JSONObject posDetail = positionInfoService.getPosDetail(pid);
        ResultData resultData = new ResultData(true, null, 200, posDetail);

        return resultData;
    }

    @GetMapping("/getPosList")
    public ResultData getPosList(@RequestParam(value = "page", defaultValue = "1") Integer pageNum) throws ServiceException {
//        System.out.println(pageNum);
        List posList = positionInfoService.getPosList(pageNum);
        ResultData resultData = new ResultData(true, null, 200, posList);

        return resultData;
    }

    @GetMapping("/searchPos")
    public ResultData searchPos(@RequestParam(value = "key", defaultValue = "") String key,
                                @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "location", defaultValue = "") String location,
                                @RequestParam(value = "filter", defaultValue = "undefined") String filter) throws ServiceException {
        System.out.println(key + "----" + pageNum + "----" + location + "----" + filter);
        if (location.equals("undefined")) {
            location = null;
        }
        if (filter.equals("undefined"))
            filter = "";
        if (key.equals("undefined"))
            key = "";
        JSONObject jsonObject = JSONObject.parseObject(filter);
        List list = positionInfoService.searchPos(key, pageNum, location, jsonObject);
        ResultData resultData = new ResultData(true, null, 200, list);

        return resultData;
    }
}
