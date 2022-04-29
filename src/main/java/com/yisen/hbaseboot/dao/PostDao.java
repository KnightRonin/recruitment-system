package com.yisen.hbaseboot.dao;

import com.yisen.hbaseboot.util.exception.DaoException;

import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/5 14:06
 */
public interface PostDao {
    void postSingleRecord(Map<String, String> map) throws DaoException;
}
