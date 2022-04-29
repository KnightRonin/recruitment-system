package com.yisen.hbaseboot.dao;

import com.yisen.hbaseboot.util.exception.DaoException;

import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/27 10:07
 */
public interface JobAnalyzeDao {
    List<Map<String, String>> getExpScal(String region, String level, String position) throws DaoException;

    List<Map<String, String>> getCompanySize(String region, String level, String position) throws DaoException;

    List<Map<String, String>> getEduInfo(String region, String level, String position) throws DaoException;

    List<Map<String, String>> getCompanyWelfare(String region, String level, String position) throws DaoException;

    List<Map<String, String>> getCompanyFinance(String region, String level, String position) throws DaoException;

    List<Map<String, String>> getCountryExpScalEdu() throws DaoException;
}
