package com.yisen.hbaseboot.dao;

import com.yisen.hbaseboot.util.exception.DaoException;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/5 15:26
 */
public interface IfDao {
    int ifPositionId(String id) throws DaoException;
}
