package com.yisen.hbaseboot.dao;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/29 18:54
 */
public interface PositionInfoDao {
    /**
     * 获取单条职位信息详情
     *
     * @param pid 对应positionID
     * @return
     */
    Map<String, String> getPosDetail(String pid);

    Map<String, Collection> getClassification(String city);

    List<Map<String, String>> getPosList();

    /**
     * 根据条件获取分页数据
     *
     * @param key
     * @param location
     * @param jsonObject
     * @return
     */
    @Deprecated
    List<Map<String, String>> searchPos(String key, String location, JSONObject jsonObject);
}
