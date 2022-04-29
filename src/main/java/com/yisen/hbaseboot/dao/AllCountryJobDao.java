package com.yisen.hbaseboot.dao;

import com.yisen.hbaseboot.util.exception.DaoException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen, luWei
 * @Date: 2020/6/4 15:43
 */
public interface AllCountryJobDao {
    /**
     * 返回全国指定日期前31天每天的职位变化信息
     *
     * @param date
     * @param family
     * @param col
     * @return
     * @throws DaoException
     */
    Map<String, Integer> getCountryEveryDay(String date, String family, String col) throws DaoException;

    /**
     * 返回与学历相关的职位信息
     *
     * @param family
     * @param education
     * @param thirdType
     * @return
     * @throws DaoException
     */
    List<Map<String, Integer>> getEducationPosition(String family, String education, String thirdType) throws DaoException;

    /**
     * 返回排行相关信息。
     *
     * @return list中每一个map都是一行数据，map<列名,列值>
     */
    List<Map<String, String>> getTopDetails(String family, String date,
                                            String[] secondType, String region,
                                            String... col) throws DaoException;

    /**
     * 获取所有公司规模数据，并统计公司规模不同的公司分组数量情况
     *
     * @param family
     * @param col
     * @return
     */
    Map<String, Integer> getCompanyScale(String family, String col) throws DaoException;

    /**
     * 全国公司福利情况统计
     *
     * @param family
     * @param col
     * @return
     */
    Map<String, Integer> getCompanyWelfare(String family, String col) throws DaoException;

}
