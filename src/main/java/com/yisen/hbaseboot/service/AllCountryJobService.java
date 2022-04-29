package com.yisen.hbaseboot.service;

import com.yisen.hbaseboot.entity.EduData;
import com.yisen.hbaseboot.entity.JobData;
import com.yisen.hbaseboot.entity.TopDetailsData;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;

import java.util.List;

/**
 * @AuthorList: LiuYiSen, LuWei
 * @Date: 2020/6/4 19:19
 */
public interface AllCountryJobService {
    /**
     * /AllProvince_DayJob_Servlet接口  业务逻辑方法
     *
     * @param date
     * @return
     * @throws DaoException
     */
    List<JobData> getCountryEveryDay(String date) throws DaoException;

    /**
     * /CountryJobEdu_Top_Servlet接口 业务逻辑方法
     *
     * @return
     * @throws DaoException
     */
    EduData getEducationPosition() throws DaoException;

    /**
     * 该接口返回指定日期[date] 年月或所有时间[type] 相对职位[position] 在不同地区[region] 的详情
     *
     * @param date     查询的时间 2020-5-11
     * @param type     查询的时间类型 Day Month Year All
     * @param position 查询的职位名称 后端开发
     * @param level    查询的下级城市等级   1 省 2 市 3 区
     * @param region   查询的区域 广东省
     * @return
     * @throws ServiceException
     * @throws DaoException
     */
    TopDetailsData getTopDetails(String date, String type, String position, Integer level, String region) throws ServiceException, DaoException;

    /**
     * /CounterCompanyScale_Servlet 接口
     * 返回公司规模不同的公司分组数量情况
     *
     * @return
     */
    List<JobData> getCompanyScale() throws DaoException;

    /**
     * /CountryJobWelfare_Servlet 接口
     * 全国公司福利情况统计
     *
     * @return
     */
    List<JobData> getCompanyWelfare() throws DaoException;
}
