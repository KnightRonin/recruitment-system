package com.yisen.hbaseboot.dao;

import com.yisen.hbaseboot.util.exception.DaoException;

import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/12 15:16
 */
public interface LabDao {
    /**
     * 读取表格所需数据
     */
    List<Map<String, String>> getTableEnd(String family, Map<String, List<String>> colMap) throws DaoException;

    /**
     * 数据可视化
     *
     * @param family  列族
     * @param colList 所有列名
     * @param times   所有事件刻度
     * @return list是所有数据，map是一行数据
     */
    List<Map<String, String>> getTimesData(String family, List<String> colList, List<String> times) throws DaoException;

    /**
     * 返回指定日期，指定的职位信息
     *
     * @param times
     * @param positions
     * @return
     */
    List<Map<String, String>> getTimePosition(List<String> times, List<String> positions);
}
