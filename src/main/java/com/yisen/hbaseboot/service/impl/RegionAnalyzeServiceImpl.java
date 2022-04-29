package com.yisen.hbaseboot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.dao.RegionAnalyzeDao;
import com.yisen.hbaseboot.service.RegionAnalyzeService;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/9/10 14:43
 */
@Service
public class RegionAnalyzeServiceImpl implements RegionAnalyzeService {
    @Autowired
    RegionAnalyzeDao regionAnalyzeDao;

    /**
     * 说明: 调用此方法，获取 当前省份 当前前五名的招聘职位 最近一周的数据
     *
     * @param region
     * @return
     * @throws DaoException
     */
    @Override
    public JSONArray provTrend(String region) throws DaoException {
//        LocalDate localDate = LocalDate.now();// 获取当前日期
        LocalDate localDate = LocalDate.of(2022, 3, 22);// 自己设置日期
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        ArrayList<String> dateList = new ArrayList<>();
        dateList.add(dateTimeFormatter.format(localDate));
        for (int i = 0; i < 6; i++) {
            localDate = localDate.plusDays(-1);
            dateList.add(dateTimeFormatter.format(localDate));
        }
        List<Map<String, String>> list = regionAnalyzeDao.provTrend(region, dateList);// 获取数据
        Map<String, Integer> secondTypeMap = DataService.columnClassSum(list, "secondType");// 统计职位数量
        Map<String, Integer> valueSortMap = DataService.getValueSortMap(secondTypeMap, 5);// 职位排序
        Set<String> keys = valueSortMap.keySet();
        JSONArray jsonArray = new JSONArray();
        for (String key : keys) {
            for (String date : dateList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", key);
                jsonObject.put("date", date);
                jsonObject.put("value", 0);
                jsonArray.add(jsonObject);
            }
        }
        for (Map<String, String> map : list) {
            String secondType = map.get("secondType");
            if (keys.contains(secondType)) {
                String date = map.get("createTime");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject o = (JSONObject) jsonArray.get(i);
                    if (o.get("type").equals(secondType) && o.get("date").equals(date)) {
                        o.put("value", ((int) o.get("value")) + 1);
                        break;
                    }
                }
            }
        }

        return jsonArray;
    }

    @Override
    public JSONObject getStandard(String region) throws DaoException {
        //=================================================全国数据统计=================================================================
        List<Map<String, String>> nationwide_standard = regionAnalyzeDao.getStandard();
        ArrayList<Integer> nationwide_salary = new ArrayList<>();
        HashSet<String> nationwide_workYear = new HashSet<>();
        HashSet<String> nationwide_education = new HashSet<>();
        HashSet<String> nationwide_financeStage = new HashSet<>();
        HashSet<String> nationwide_companySize = new HashSet<>();
        HashSet<String> nationwide_jobNature = new HashSet<>();
        for (Map<String, String> map : nationwide_standard) { // 对省份数据进行遍历统计
            String salary_s = map.get("salary");
//                System.out.println(salary_s);
            if (salary_s.contains("k")) {
                salary_s = salary_s.substring(salary_s.indexOf("-") + 1, salary_s.indexOf("k", salary_s.indexOf("-") + 1));
            } else {
                salary_s = salary_s.substring(salary_s.indexOf("-") + 1, salary_s.indexOf("K", salary_s.indexOf("-") + 1));
            }
            nationwide_salary.add(Integer.parseInt(salary_s));
            nationwide_workYear.add(map.get("workYear"));// 统计工作经验
            nationwide_education.add(map.get("education"));// 统计学历
            nationwide_financeStage.add(map.get("financeStage"));// 统计融资情况
            nationwide_companySize.add(map.get("companySize"));// 统计公司规模
            nationwide_jobNature.add(map.get("jobNature"));// 统计工作性质
        }
        nationwide_salary.sort(((o1, o2) -> o1 - o2));
        ArrayList<String> nationwide_resultSalary = new ArrayList<>(); // 省份工资结果统计
        int size = nationwide_salary.size() / 5;
        for (int i = 0; i < 5; i++) {
            List<Integer> integers = null;
            if (i < 4) {
                integers = nationwide_salary.subList(i * size, (i + 1) * size);
            } else {
                integers = nationwide_salary.subList(i * size, nationwide_salary.size());
            }
            Double average = integers.stream().collect(Collectors.averagingInt(obj -> obj));
            String format = String.format("%.1f", average);
            nationwide_resultSalary.add(format);
        }
        //===========================================================省份数据统计=================================================
        List<Map<String, String>> province_standard = regionAnalyzeDao.getStandard(region);
        ArrayList<Integer> province_salary = new ArrayList<>();
        HashSet<String> province_workYear = new HashSet<>();
        HashSet<String> province_education = new HashSet<>();
        HashSet<String> province_financeStage = new HashSet<>();
        HashSet<String> province_companySize = new HashSet<>();
        HashSet<String> province_jobNature = new HashSet<>();
        for (Map<String, String> map : province_standard) { // 对省份数据进行遍历统计
            String salary_s = map.get("salary");
//                System.out.println(salary_s);
            if (salary_s.contains("k")) {
                salary_s = salary_s.substring(salary_s.indexOf("-") + 1, salary_s.indexOf("k", salary_s.indexOf("-") + 1));
            } else {
                salary_s = salary_s.substring(salary_s.indexOf("-") + 1, salary_s.indexOf("K", salary_s.indexOf("-") + 1));
            }
            province_salary.add(Integer.parseInt(salary_s));
            province_workYear.add(map.get("workYear"));// 统计工作经验
            province_education.add(map.get("education"));// 统计学历
            province_financeStage.add(map.get("financeStage"));// 统计融资情况
            province_companySize.add(map.get("companySize"));// 统计公司规模
            province_jobNature.add(map.get("jobNature"));// 统计工作性质
        }
        province_salary.sort(((o1, o2) -> o1 - o2));
        ArrayList<String> province_resultSalary = new ArrayList<>(); // 省份工资结果统计
        size = province_salary.size() / 5;
        for (int i = 0; i < 5; i++) {
            List<Integer> integers = null;
            if (i < 4) {
                integers = province_salary.subList(i * size, (i + 1) * size);
            } else {
                integers = province_salary.subList(i * size, province_salary.size());
            }
            Double average = integers.stream().collect(Collectors.averagingInt(obj -> obj));
            String format = String.format("%.1f", average);
            province_resultSalary.add(format);
        }

        JSONObject jsonObject = new JSONObject();// 进行结果添加
        jsonObject.put("salaryList", province_resultSalary);
        jsonObject.put("workYearList", province_workYear);
        jsonObject.put("educationList", province_education);
        jsonObject.put("financeStageList", province_financeStage);
        jsonObject.put("companySizeList", province_companySize);
        jsonObject.put("jobNatureList", province_jobNature);

        jsonObject.put("partSalaryList", nationwide_resultSalary);
        jsonObject.put("partWorkYearList", nationwide_workYear);
        jsonObject.put("partEducationList", nationwide_education);
        jsonObject.put("partFinanceStageList", nationwide_financeStage);
        jsonObject.put("partCompanySizeList", nationwide_companySize);
        jsonObject.put("partJobNatureList", nationwide_jobNature);
        return jsonObject;
    }

    @Override
    public JSONArray getScatter(String region) throws DaoException {
        JSONArray jsonArray = new JSONArray();// 返回结果
//        LocalDate localDate = LocalDate.now();// 获取当前日期
        LocalDate localDate = LocalDate.of(2022, 3, 29); // 拟定当前日期
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        //======================================================今日数据统计============================================
        List<Map<String, String>> day_list = regionAnalyzeDao.getScatter(region, dateTimeFormatter.format(localDate));// 获取今日数据
        Map<String, Integer> day_secondTypeMap = DataService.columnClassSum(day_list, "secondType");
        Map<String, Integer> day_valueSortMap = DataService.getValueSortMap(day_secondTypeMap, 10);// 找出排名前十
        HashMap<String, Integer> day_average = new HashMap<>();// 统计今日各个职位平均工资
        int day_secondTypeSum = 0;
        for (String key : day_valueSortMap.keySet()) {
            day_average.put(key, 0);
            day_secondTypeSum += day_valueSortMap.get(key);
        }
        for (Map<String, String> map : day_list) {// 统计总工资
            String secondType = map.get("secondType");
            if (day_valueSortMap.containsKey(secondType)) {
                String salary = map.get("salary");
                if (salary.contains("k")) {
                    salary = salary.substring(salary.indexOf("-") + 1, salary.indexOf("k", salary.indexOf("-") + 1));
                } else {
                    salary = salary.substring(salary.indexOf("-") + 1, salary.indexOf("K", salary.indexOf("-") + 1));
                }
                day_average.put(secondType, day_average.get(secondType) + (Integer.parseInt(salary) * 1000));
            }
        }
        JSONArray day_array = new JSONArray();
        jsonArray.add(day_array);
        for (String key : day_valueSortMap.keySet()) {
            Integer secondTypeNum = day_secondTypeMap.get(key);
            int average = day_average.get(key) / secondTypeNum;
            JSONArray arr = new JSONArray();
            arr.add(average);
            BigDecimal bigDecimal = new BigDecimal(secondTypeNum / (day_secondTypeSum * 1.0) * 100);
            arr.add(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            arr.add(secondTypeNum);
            arr.add(key);
            arr.add("今日");
            day_array.add(arr);
        }
        //==================================================昨日数据统计===================================================
        localDate = localDate.plusDays(-1);
        List<Map<String, String>> yesterday_list = regionAnalyzeDao.getScatter(region, dateTimeFormatter.format(localDate), day_valueSortMap.keySet());// 获取今日数据
        Map<String, Integer> yesterday_secondTypeMap = DataService.columnClassSum(yesterday_list, "secondType");
        Map<String, Integer> yesterday_valueSortMap = yesterday_secondTypeMap;// 找出排名前十
        HashMap<String, Integer> yesterday_average = new HashMap<>();// 统计今日各个职位平均工资
        int secondTypeSum = 0;
        for (String key : yesterday_valueSortMap.keySet()) {
            yesterday_average.put(key, 0);
            secondTypeSum += yesterday_valueSortMap.get(key);
        }
        for (Map<String, String> map : yesterday_list) {// 统计总工资
            String secondType = map.get("secondType");
            if (yesterday_valueSortMap.containsKey(secondType)) {
                String salary = map.get("salary");
                if (salary.contains("k")) {
                    salary = salary.substring(salary.indexOf("-") + 1, salary.indexOf("k", salary.indexOf("-") + 1));
                } else {
                    salary = salary.substring(salary.indexOf("-") + 1, salary.indexOf("K", salary.indexOf("-") + 1));
                }
                yesterday_average.put(secondType, yesterday_average.get(secondType) + (Integer.parseInt(salary) * 1000));
            }
        }
        JSONArray yesterday_array = new JSONArray();
        jsonArray.add(yesterday_array);
        for (String key : day_valueSortMap.keySet()) {
            Integer secondTypeNum = yesterday_secondTypeMap.get(key);
            int average = 0;
            BigDecimal bigDecimal = new BigDecimal(0.00);
            if (secondTypeNum != null) {
                average = yesterday_average.get(key) / secondTypeNum;
                bigDecimal = new BigDecimal(secondTypeNum / (secondTypeSum * 1.0) * 100);
            } else {
                secondTypeNum = 0;
            }

            JSONArray arr = new JSONArray();
            arr.add(average);
            arr.add(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            arr.add(secondTypeNum);
            arr.add(key);
            arr.add("昨日");
            yesterday_array.add(arr);
        }


        return jsonArray;
    }
}
