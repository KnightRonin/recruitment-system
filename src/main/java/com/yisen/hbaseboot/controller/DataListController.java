package com.yisen.hbaseboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.entity.EduData;
import com.yisen.hbaseboot.entity.JobData;
import com.yisen.hbaseboot.entity.TopDetailsData;
import com.yisen.hbaseboot.service.AllCountryJobService;
import com.yisen.hbaseboot.service.AllProvinceJobService;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.exception.ControllerException;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * @AuthorList: LuWei, LiuYiSen
 */
@RestController
public class DataListController {

    //    @PostMapping("/CountryDate_Job_Servlet")
//    public ResultData getTimeScale(){
//        ResultData<Object> resultData = new ResultData<>();
//
//        return resultData;
//    }
    @Autowired
    AllProvinceJobService allProvinceJobService;
    @Autowired
    AllCountryJobService allCountryJobService;

    /**
     * @description: 此接口用来获取   全国各个省份所有招聘职位的数量总和
     */
    @GetMapping("/AllProvince_Job_Servlet")
    public ResultData<List<JobData>> getCountryData() throws DaoException {
        long time = System.currentTimeMillis();
        ResultData<List<JobData>> resultData = null;

        List<JobData> list = allProvinceJobService.getAllProvinceJob();
        resultData = new ResultData<List<JobData>>(true, "查询成功", 200, list);

        System.out.println(System.currentTimeMillis() - time);
        return resultData;
    }

    /**
     * @description: 此接口用来获取   全国最近一个月每日职位数量
     */
    @GetMapping("/AllProvince_DayJob_Servlet")
    public ResultData<JobData> getMonthEveryDay(@RequestParam(value = "date", defaultValue = "") String date) throws DaoException, ControllerException {
        Long time = System.currentTimeMillis();
        ResultData resultData = null;
        if (date == null || date.equals("")) {
            date = LocalDate.now().toString();

        }
        List<JobData> list = allCountryJobService.getCountryEveryDay(date);
        resultData = new ResultData(true, "查询成功", 200, list);
        System.out.println(System.currentTimeMillis() - time);
        return resultData;
    }

    /**
     * @return
     * @throws DaoException
     * @description: 此接口用来获取职位排名信息
     */
    @GetMapping("/CountryJobEdu_Top_Servlet")
    public ResultData getEducationPosition() throws DaoException {
        Long time = System.currentTimeMillis();
        ResultData resultData = null;
        EduData eduData = allCountryJobService.getEducationPosition();
        resultData = new ResultData(true, "查询成功", 200, eduData);
        System.out.println(System.currentTimeMillis() - time);
        return resultData;
    }

    @GetMapping("/CountryJob_DayTop_Servlet")
    public ResultData getTopDetails(@RequestParam(value = "date", defaultValue = "") String date,
                                    @RequestParam(value = "type", defaultValue = "All") String type,
                                    @RequestParam(value = "position", defaultValue = "") String position,
                                    @RequestParam(value = "level", defaultValue = "2") Integer level,
                                    @RequestParam(value = "region", defaultValue = "") String region) throws ServiceException, DaoException {
        TopDetailsData detailsData = allCountryJobService.getTopDetails(date, type, position, level, region);
//        detailsData.setDesc("后端开发", "TOP1", 323, 4568);
        ResultData<TopDetailsData> resultData = new ResultData<>(true, "success", 200, detailsData);
        return resultData;
    }

    /**
     * 此接口用来获取公司规模统计情况
     *
     * @return
     * @throws DaoException
     */
    @GetMapping("/CounterCompanyScale_Servlet")
    public ResultData getCompanyScale() throws DaoException {
        List<JobData> list = allCountryJobService.getCompanyScale();
        ResultData<List<JobData>> resultData = new ResultData<>(true, null, 200, list);
        return resultData;
    }

    /**
     * 此接口用来获取公司福利统计情况
     *
     * @return
     * @throws DaoException
     */
    @GetMapping("/CountryJobWelfare_Servlet")
    public ResultData getCompanyWelfare() throws DaoException {
        List<JobData> list = allCountryJobService.getCompanyWelfare();
        ResultData<List<JobData>> resultData = new ResultData<>(true, null, 200, list);
        return resultData;
    }

    @GetMapping("/Language_Servlet")
    public ResultData languageServlet() throws IOException {
        // 读取并添加 language
        InputStream inputStream = this.getClass().getResourceAsStream("/static/language.json");
        byte[] filecontent = new byte[inputStream.available()];
        inputStream.read(filecontent);
        inputStream.close();
        String str = new String(filecontent);
        JSONArray object = JSONObject.parseArray(str);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("change", object);
        // 读取并添加 rank
        inputStream = this.getClass().getResourceAsStream("/static/rank.json");
        filecontent = new byte[inputStream.available()];
        inputStream.read(filecontent);
        inputStream.close();
        str = new String(filecontent);
        object = JSONObject.parseArray(str);
        jsonObject.put("rank", object);
        return new ResultData(true, "访问成功", 200, jsonObject);
        //new InputStreamReader();
    }
}
