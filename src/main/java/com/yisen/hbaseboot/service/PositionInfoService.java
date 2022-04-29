package com.yisen.hbaseboot.service;

import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.util.exception.ServiceException;

import java.util.List;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/29 19:04
 */
public interface PositionInfoService {
    /**
     * 获取单条记录详情
     *
     * @param pid 记录id
     * @return
     */
    JSONObject getPosDetail(String pid) throws ServiceException;

    /**
     * 所有数据分页展示
     *
     * @param pageNum
     * @return
     */
    List getPosList(int pageNum) throws ServiceException;

    List searchPos(String key, Integer pageNum, String location, JSONObject jsonObject) throws ServiceException;
}
