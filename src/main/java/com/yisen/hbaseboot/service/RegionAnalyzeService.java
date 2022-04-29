package com.yisen.hbaseboot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.util.exception.DaoException;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/9/10 14:42
 */
public interface RegionAnalyzeService {
    /**
     * 说明: 调用此方法，获取 当前省份 当前前五名的招聘职位 最近一周的数据
     *
     * @param region
     * @return
     * @throws DaoException
     */
    JSONArray provTrend(String region) throws DaoException;

    JSONObject getStandard(String region) throws DaoException;

    JSONArray getScatter(String region) throws DaoException;
}
