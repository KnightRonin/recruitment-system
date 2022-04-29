package com.yisen.hbaseboot.service;

import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.util.exception.DaoException;

import java.util.List;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/27 14:56
 */
public interface JobAnalyzeService {
    /**
     * 获取经验与薪资封装后的对象
     *
     * @param region
     * @param level
     * @param position
     * @return
     */
    List<JSONObject> getExpScal(String region, String level, String position) throws DaoException;

    /**
     * 根据条件获取公司规模信息
     *
     * @param region   地区
     * @param level    地区级别
     * @param position 职位
     * @return
     */
    List<JSONObject> getCompanySize(String region, String level, String position) throws DaoException;

    /**
     * 根据条件获取学历相关信息
     *
     * @param region   地区
     * @param level    地区级别
     * @param position 职位
     * @return
     */
    List<JSONObject> getEduInfo(String region, String level, String position) throws DaoException;

    /**
     * 根据条件获取公司福利情况
     *
     * @param region   地区
     * @param level    地区级别
     * @param position 职位
     * @return
     * @throws DaoException
     */
    List<JSONObject> getCompanyWelfare(String region, String level, String position) throws DaoException;

    /**
     * 根据条件获取公司融资情况
     *
     * @param region
     * @param level
     * @param position
     * @return
     * @throws DaoException
     */
    List<JSONObject> getCompanyFinance(String region, String level, String position) throws DaoException;

    /**
     * 获取 工作经验-学历-薪资的关系
     *
     * @return
     */
    JSONObject getCountryExpScalEdu() throws DaoException;
}
