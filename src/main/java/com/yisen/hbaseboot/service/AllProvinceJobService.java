package com.yisen.hbaseboot.service;

import com.yisen.hbaseboot.entity.JobData;
import com.yisen.hbaseboot.util.exception.DaoException;

import java.io.IOException;
import java.util.List;

public interface AllProvinceJobService {
    List<JobData> getAllProvinceJob() throws DaoException;
}
