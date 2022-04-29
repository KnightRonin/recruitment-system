package com.yisen.hbaseboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.entity.TableData;
import com.yisen.hbaseboot.service.LabService;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.exception.ControllerException;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen，LuWei
 * @Date: 2020/6/11 18:01
 */
@RestController
public class LabelController {
    @Autowired
    LabService labService;

    @PostMapping("/TableEnd_Servlet")
    public ResultData getTableEnd(@RequestBody(required = true) Map<String, Object> map) throws ControllerException, DaoException, ServiceException {
        JSONObject jsonObject = new JSONObject(map);
        String time = jsonObject.getString("time");
        JSONArray time1 = jsonObject.getJSONArray("time");
        if (time.equals("[]") | time.equals("")) {
            throw new ControllerException("time 不能为空！");
        }
        List<List<TableData>> tableEnd = labService.getTableEnd(jsonObject);
        ResultData resultData = new ResultData(true, null, 200, tableEnd);
        return resultData;
    }

    @PostMapping("/CountryDate_Job_Servlet")
    public ResultData getTimesData(@RequestBody(required = true) Map<String, Object> map) throws DaoException, ParseException {
        JSONObject jsonObject = new JSONObject(map);
        List<String> times = (List<String>) jsonObject.get("time");
        List<JSONObject> timesData = labService.getTimesData(times);
        ResultData resultData = new ResultData(true, null, 200, timesData);
        return resultData;
    }

    @PostMapping("/CountryDatePosition")
    public ResultData getTimePosition(@RequestBody(required = true) Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject(map);
        List<String> times = (List<String>) jsonObject.get("time");
        List<String> positions = (List<String>) jsonObject.get("position");
        List<JSONObject> timePosition = labService.getTimePosition(times, positions);
        ResultData resultData = new ResultData(true, null, 200, timePosition);
        return resultData;
    }

}
