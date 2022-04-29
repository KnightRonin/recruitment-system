package com.yisen.hbaseboot.service;

import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/5 14:56
 */
public interface PostService {
    void postSingleRecord(Map<String, String> map) throws DaoException, ServiceException;

    void importData() throws IOException, DaoException;
}
