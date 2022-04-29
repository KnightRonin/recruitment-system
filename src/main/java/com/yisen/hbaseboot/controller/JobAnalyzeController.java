package com.yisen.hbaseboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.service.JobAnalyzeService;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/26 16:32
 */
@RestController
public class JobAnalyzeController {
    @Autowired
    JobAnalyzeService jobAnalyzeService;

    @GetMapping("/Exp_Scalary_Servlet")
    public ResultData getExpScal(@RequestParam(value = "region", defaultValue = "") String region,
                                 @RequestParam(value = "level", defaultValue = "2") String level,
                                 @RequestParam(value = "position", defaultValue = "") String position) throws DaoException {
        List<JSONObject> expScal = jobAnalyzeService.getExpScal(region, level, position);
        ResultData resultData = new ResultData(true, null, 200, expScal);
        return resultData;
    }

    @GetMapping("/CompanySize_Servlet")
    public ResultData getCompanySize(@RequestParam(value = "region", defaultValue = "") String region,
                                     @RequestParam(value = "level", defaultValue = "2") String level,
                                     @RequestParam(value = "position", defaultValue = "") String position) throws DaoException {
        List<JSONObject> list = jobAnalyzeService.getCompanySize(region, level, position);
        ResultData resultData = new ResultData(true, null, 200, list);
        return resultData;
    }

    @GetMapping("/EduInfo_Servlet")
    public ResultData getEduInfo(@RequestParam(value = "region", defaultValue = "") String region,
                                 @RequestParam(value = "level", defaultValue = "2") String level,
                                 @RequestParam(value = "position", defaultValue = "") String position) throws DaoException {
        List<JSONObject> list = jobAnalyzeService.getEduInfo(region, level, position);
        ResultData resultData = new ResultData(true, null, 200, list);
        return resultData;
    }

    @GetMapping("/CompanyWelfare_Servlet")
    public ResultData getCompanyWelfare(@RequestParam(value = "region", defaultValue = "") String region,
                                        @RequestParam(value = "level", defaultValue = "2") String level,
                                        @RequestParam(value = "position", defaultValue = "") String position) throws DaoException {
        List<JSONObject> list = jobAnalyzeService.getCompanyWelfare(region, level, position);
        ResultData resultData = new ResultData(true, null, 200, list);
        return resultData;
    }

    @GetMapping("/CompanyFinance_Servlet")
    public ResultData getCompanyFinance(@RequestParam(value = "region", defaultValue = "") String region,
                                        @RequestParam(value = "level", defaultValue = "2") String level,
                                        @RequestParam(value = "position", defaultValue = "") String position) throws DaoException {
        List<JSONObject> list = jobAnalyzeService.getCompanyFinance(region, level, position);
        ResultData resultData = new ResultData(true, null, 200, list);
        return resultData;
    }

    @GetMapping("/CountryExpScalaryEdu_Servlet")
    public ResultData getCountryExpScalEdu() throws DaoException {
        JSONObject list = jobAnalyzeService.getCountryExpScalEdu();
        ResultData resultData = new ResultData(true, null, 200, list);
        return resultData;
    }

}
