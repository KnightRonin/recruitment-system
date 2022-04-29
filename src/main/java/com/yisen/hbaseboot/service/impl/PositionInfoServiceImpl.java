package com.yisen.hbaseboot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.yisen.hbaseboot.dao.PositionInfoDao;
import com.yisen.hbaseboot.entity.Position;
import com.yisen.hbaseboot.mapper.SelectMapper;
import com.yisen.hbaseboot.service.PositionInfoService;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/29 19:07
 */
@Service
public class PositionInfoServiceImpl implements PositionInfoService {
    @Autowired
    PositionInfoDao positionInfoDao;
    @SuppressWarnings("all")
//    @Autowired
//    SelectMapper selectMapper;
    @Autowired
    SelectMapper selectMapper;

    /**
     * 获取单条记录详情
     *
     * @param pid 记录id
     * @return
     */
    @Override
    public JSONObject getPosDetail(String pid) throws ServiceException {
        Position posDetail = selectMapper.getPosDetail(pid);
        if (posDetail == null) {
            throw new ServiceException("没有此条信息", 400);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("positionId", posDetail.getPositionId());
        jsonObject.put("positionName", posDetail.getPositionName());
        jsonObject.put("companyShortName", posDetail.getCompanyShortName());
        jsonObject.put("companyLogo", posDetail.getCompanyLogo());
        jsonObject.put("city", posDetail.getCity());
        jsonObject.put("district", posDetail.getDistrict());
        jsonObject.put("workYear", posDetail.getWorkYear());
        jsonObject.put("education", posDetail.getEducation());
        jsonObject.put("salary", posDetail.getSalary());
        jsonObject.put("famousCompany", posDetail.getFamousCompany());
        jsonObject.put("jobNature", posDetail.getJobNature());
        jsonObject.put("longitude", posDetail.getLongitude());
        jsonObject.put("latitude", posDetail.getLatitude());
        jsonObject.put("companyLabelList", JSONArray.parseArray(posDetail.getCompanyLabelList()));
        jsonObject.put("financeStage", posDetail.getFinanceStage());
        jsonObject.put("industryField", posDetail.getIndustryField());
        jsonObject.put("companySize", posDetail.getCompanySize());
        jsonObject.put("positionLables", JSONArray.parseArray(posDetail.getPositionLables()));
        jsonObject.put("secondType", posDetail.getSecondType());
        jsonObject.put("thirdType", posDetail.getThirdType());
        jsonObject.put("positionDesc", JSONArray.parseArray(posDetail.getPositionDesc()));

        // 所有数据去重复后
        Map<String, Collection> classification = positionInfoDao.getClassification(posDetail.getCity());
        ArrayList<Integer> salary = (ArrayList<Integer>) classification.get("salary");
        salary.sort(((o1, o2) -> Integer.parseInt(String.valueOf(o1)) - Integer.parseInt(String.valueOf(o2))));
        int size = salary.size() / 5;
        ArrayList<String> resultSalary = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<Integer> integers = null;
            if (i < 4) {
                integers = salary.subList(i * size, (i + 1) * size);
            } else {
                integers = salary.subList(i * size, salary.size());
            }
            Double average = integers.stream().collect(Collectors.averagingInt(obj -> obj));
            String format = String.format("%.1f", average);
            resultSalary.add(format);
        }

        jsonObject.put("salaryList", resultSalary);
        jsonObject.put("workYearList", JSONArray.parseArray("['不限','应届毕业生','1年以下','1-3年','3-5年','5-10年','10年以上']"));
        jsonObject.put("educationList", JSONArray.parseArray("['不限', '本科', '大专', '硕士', '博士']"));
        jsonObject.put("financeStageList", JSONArray.parseArray("['不需要融资','A轮','未融资','B轮','天使轮','上市公司','D轮及以上','C轮'" +
                "]"));
        jsonObject.put("companySizeList", JSONArray.parseArray("['50-150人','15-50人','500-2000人'," +
                "'150-500人','2000人以上','少于15人','10-50人','2000-5000人'" +
                "]"));
        jsonObject.put("jobNatureList", JSONArray.parseArray("['全职', '实习', '兼职']"));
        return jsonObject;
    }

    /**
     * 所有数据分页展示
     *
     * @param pageNum
     * @return
     */
    @Override
    public List getPosList(int pageNum) throws ServiceException {
        PageHelper.startPage(pageNum, 10);
        List<Position> posList = selectMapper.getPosList();
        List<JSONObject> list = new ArrayList<>();
        for (Position position : posList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("positionId", position.getPositionId());
            jsonObject.put("positionName", position.getPositionName());
            jsonObject.put("companyShortName", position.getCompanyShortName());
            jsonObject.put("companyLogo", position.getCompanyLogo());
            jsonObject.put("city", position.getCity());
            jsonObject.put("workYear", position.getWorkYear());
            jsonObject.put("education", position.getEducation());
            jsonObject.put("famousCompany", position.getFamousCompany());
            jsonObject.put("salary", position.getSalary());
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public List searchPos(String key, Integer pageNum, String location, JSONObject jsonObject) throws ServiceException {
        PageHelper.startPage(pageNum, 10);
        Map<String, Object> innerMap = new HashMap<>();
        Map<String, List<String>> map = new HashMap<>();
        String leftSalary = null;
        String rightSalary = null;
        if (jsonObject != null) {
            innerMap = jsonObject.getInnerMap();
            for (String k : innerMap.keySet()) {
                if (k.equals("salary")) {
                    String salary = innerMap.remove("salary").toString();
                    salary = salary.substring(2, salary.length() - 2);
                    if (salary.contains("k以上")) {
                        leftSalary = salary.substring(0, salary.indexOf("k"));
                        rightSalary = "0";
                    } else {
                        leftSalary = salary.substring(0, salary.indexOf("k") != -1 ? salary.indexOf("k") : salary.indexOf("K"));
                        rightSalary = salary.substring(salary.indexOf("-") + 1, salary.length() - 1);
                    }
                    continue;
                }
                map.put(k, DataService.parseStrArray(innerMap.get(k).toString()));
            }
        }
        List<Map<String, String>> list = selectMapper.searchPos(key, location, innerMap, leftSalary, rightSalary);
        return list;
    }
}
