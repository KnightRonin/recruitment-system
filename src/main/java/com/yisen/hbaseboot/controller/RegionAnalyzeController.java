package com.yisen.hbaseboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.service.RegionAnalyzeService;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/9/10 14:16
 */
@RestController
public class RegionAnalyzeController {

    @Autowired
    RegionAnalyzeService regionAnalyzeService;

    /**
     * 说明: 调用此接口，获取 当前省份 当前前五名的招聘职位 最近一周的数据
     *
     * @param region
     * @return
     * @throws DaoException
     */
    @GetMapping("/provTrend")
    public ResultData provTrend(@RequestParam("region") String region) throws DaoException {
        ResultData resultData = null;
        JSONArray jsonArray = regionAnalyzeService.provTrend(region);
        resultData = new ResultData(true, "查询成功", 200, jsonArray);
        return resultData;
    }

    /**
     * 说明： 返回全国及当地的六项数据水平数据
     *
     * @param region
     * @return
     * @throws DaoException
     */
    @GetMapping("/getStandard")
    public ResultData getStandard(@RequestParam("region") String region) throws DaoException {
        ResultData resultData = null;
        JSONObject jsonObject = regionAnalyzeService.getStandard(region);
        resultData = new ResultData(true, "查询成功", 200, jsonObject);
        return resultData;
    }

    @GetMapping("/getScatter")
    public ResultData getScatter(@RequestParam("region") String region) throws DaoException {
        ResultData resultData = null;
        JSONArray jsonArray = regionAnalyzeService.getScatter(region);
        resultData = new ResultData(true, "查询成功", 200, jsonArray);
        return resultData;
    }
}
