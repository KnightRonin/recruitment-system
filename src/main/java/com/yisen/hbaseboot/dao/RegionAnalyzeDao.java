package com.yisen.hbaseboot.dao;

import com.yisen.hbaseboot.util.exception.DaoException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/9/10 14:21
 */
public interface RegionAnalyzeDao {
    List<Map<String, String>> provTrend(String region, List<String> date) throws DaoException;

    /**
     * 返回传入省份的工资，工作年份，学历要求，公司融资情况，公司大小，工作性质
     *
     * @param region
     * @return
     * @throws DaoException
     */
    List<Map<String, String>> getStandard(String region) throws DaoException;

    /**
     * 返回全国的工资，工作年份，学历要求，公司融资情况，公司大小，工作性质
     *
     * @return
     * @throws DaoException
     */
    List<Map<String, String>> getStandard() throws DaoException;

    List<Map<String, String>> getScatter(String region, String day) throws DaoException;

    List<Map<String, String>> getScatter(String region, String day, Set<String> secondType) throws DaoException;
}
