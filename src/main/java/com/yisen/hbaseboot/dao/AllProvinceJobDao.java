package com.yisen.hbaseboot.dao;

import com.yisen.hbaseboot.util.exception.DaoException;

import java.io.IOException;
import java.util.Map;

public interface AllProvinceJobDao {
    Map<String, Integer> getAllProvinceJob(String family, String col) throws DaoException;
}
