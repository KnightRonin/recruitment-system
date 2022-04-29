package com.yisen.wechat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/8/4 10:34
 */
public interface WeChatService {
    /**
     * 首页职位信息对比
     *
     * @return
     * @throws DaoException
     */
    JSONArray getCompare() throws DaoException;

    /**
     * 返回符合条件的前 5条数据
     *
     * @param salary
     * @param city
     * @param pos
     * @param jobNature
     * @param positionLables
     * @param extraId
     * @return
     */
    JSONArray matchPos(String salary, String city, String pos, String jobNature, String positionLables, String extraId);

    /**
     * 返回最近一个月 招聘数量最多的前三种职位，返回每日数量。
     *
     * @return
     */
    JSONArray posTrend(String city) throws DaoException;

    /**
     * 全国薪资区域平均 [3,7,10,15,18]
     *
     * @return
     */
    JSONArray countrySalary() throws DaoException;
}
