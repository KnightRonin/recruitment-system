package com.yisen.wechat.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.wechat.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/8/4 10:27
 */
@RestController
public class WeChatController {
    @Autowired
    WeChatService weChatService;

    @GetMapping("/getCompare")
    public ResultData getCompare() throws DaoException {
        ResultData resultData = null;
        JSONArray compare = weChatService.getCompare();
        resultData = new ResultData(true, "成功", 200, compare);
        return resultData;
    }

    @GetMapping("/matchPos")
    public ResultData matchPos(@RequestParam(value = "salary", defaultValue = "0k以上") String salary,
                               @RequestParam(value = "city", defaultValue = "") String city,
                               @RequestParam(value = "pos", defaultValue = "") String pos,
                               @RequestParam(value = "jobNature", defaultValue = "") String jobNature,
                               @RequestParam(value = "positionLables", defaultValue = "") String positionLables,
                               @RequestParam(value = "extraId", defaultValue = "") String extraId) {
        ResultData resultData = null;
        JSONArray jsonArray = weChatService.matchPos(salary, city, pos, jobNature, positionLables, extraId);
        resultData = new ResultData(true, "成功", 200, jsonArray);
        return resultData;
    }

    @GetMapping("/posTrend")
    public ResultData posTrend(@RequestParam(value = "city", defaultValue = "") String city) throws DaoException {
//        System.out.println(city);
        ResultData resultData = null;
        JSONArray jsonObject = weChatService.posTrend(city);
        resultData = new ResultData(true, "成功", 200, jsonObject);
        return resultData;
    }

    @GetMapping("/country_salary")
    public ResultData countrySalary() throws DaoException {
        JSONArray result = weChatService.countrySalary();
        ResultData resultData = null;
        resultData = new ResultData(true, "成功", 200, result);
        return resultData;
    }

    public ResultData hello(@RequestParam("key") String key) {
        ResultData resultData = null;
        resultData = new ResultData(true, "成功", 200, key);
        return resultData;
    }
}
