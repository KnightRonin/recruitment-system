package com.yisen.hbaseboot.service;

import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.entity.TableData;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;

import java.text.ParseException;
import java.util.List;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/12 16:43
 */
public interface LabService {
    /**
     * 返回表格所需数据
     *
     * @param jsonObject
     * @return
     */
    List<List<TableData>> getTableEnd(JSONObject jsonObject) throws DaoException, ServiceException;

    /**
     * 返回所有时间刻度的数据
     *
     * @param times 所有时间刻度
     * @return
     */
    List<JSONObject> getTimesData(List<String> times) throws DaoException, ParseException;

    /**
     * 返回指定日期，指定的职位信息
     *
     * @param times
     * @param position
     * @return
     */
    List<JSONObject> getTimePosition(List<String> times, List<String> position);
}
