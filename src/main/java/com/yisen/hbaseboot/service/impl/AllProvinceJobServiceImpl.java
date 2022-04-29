package com.yisen.hbaseboot.service.impl;

import com.yisen.hbaseboot.dao.AllProvinceJobDao;
import com.yisen.hbaseboot.entity.JobData;
import com.yisen.hbaseboot.service.AllProvinceJobService;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @AuthorList: LiuYiSen, LuWei
 * @Date: 2020/6/4 19:19
 */
@Service
public class AllProvinceJobServiceImpl implements AllProvinceJobService {
    @Autowired
    AllProvinceJobDao allProvinceJobDao;
    @Autowired
    String family;

    @Override
    /**
     * @Author: LiuYiSen
     * @description: 用于 /AllProvince_Job_Servlet 接口
     */
    public List<JobData> getAllProvinceJob() throws DaoException {
        Map<String, Integer> map = allProvinceJobDao.getAllProvinceJob(family, "city_province");
        return ResultService.parseList(map);
    }
}
