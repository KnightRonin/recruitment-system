package com.yisen.hbaseboot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.dao.JobAnalyzeDao;
import com.yisen.hbaseboot.service.JobAnalyzeService;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/27 14:57
 */
@Service
public class JobAnalyzeServiceImpl implements JobAnalyzeService {
    @Autowired
    JobAnalyzeDao jobAnalyzeDao;

    /**
     * 获取经验与薪资封装后的对象
     *
     * @param region
     * @param level
     * @param position
     * @return
     */
    @Override
    public List<JSONObject> getExpScal(String region, String level, String position) throws DaoException {
        List<Map<String, String>> expScal = jobAnalyzeDao.getExpScal(region, level, position);
//        System.out.println(expScal.size());
        // const salary = ['3k及以下', '4k', '6k', '8k', '10k', '12k', '14k', '16k', '20k', '24k', '28k', '30K及以上']
        int[] salarys = {3, 4, 6, 8, 10, 12, 14, 16, 20, 24, 28, 30};
        String[] workYears = {"不限", "应届毕业生", "1年以下", "1-3年", "3-5年", "5-10年", "10年以上"};
        ArrayList<String> years = new ArrayList(Arrays.asList(workYears));
        ArrayList<JSONObject> list = new ArrayList<>();
        Map<String, List<Integer>> map = new LinkedHashMap<>();
        for (String year : years) {
            ArrayList<Integer> integers = new ArrayList<>();
            for (int i = 0; i < salarys.length; i++) {
                integers.add(0);
            }
            map.put(year, integers);
        }

        for (Map<String, String> map1 : expScal) {
            String workYear = map1.get("workYear");
            // 工资处理
            String salary = map1.get("salary");
            double salary_d = 0.0;
            if (salary.contains("k")) {
                salary_d = Double.parseDouble(salary.substring(0, salary.indexOf("k")));
            } else if (salary.contains("K")) {
                salary_d = Double.parseDouble(salary.substring(0, salary.indexOf("K")));
            }
            if (map.containsKey(workYear)) { // 结果初步封装至map
                List<Integer> integers = map.get(workYear);
                int n = salarys.length - 1;
                for (int i = 0; i < salarys.length; i++) {
                    if (salary_d > salarys[i]) {
                        continue;
                    } else if (salary_d <= salarys[i]) {
                        n = i;
                        break;
                    }
                }
                integers.set(n, integers.get(n) + 1);
            }
        }
        for (String s : map.keySet()) {
            List<Integer> integers = map.get(s);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("value", integers);
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 根据条件获取公司规模信息
     *
     * @param region   地区
     * @param level    地区级别
     * @param position 职位
     * @return
     */
    @Override
    public List<JSONObject> getCompanySize(String region, String level, String position) throws DaoException {
        List<Map<String, String>> sizeList = jobAnalyzeDao.getCompanySize(region, level, position);
        // 公司规模固定分组信息
//        const companySize = ["少于15人", "15-50人", "50-150人", "150-500人", "500-2000人", "2000人以上"]
        String[] companySizes = {"少于15人", "15-50人", "50-150人", "150-500人", "500-2000人", "2000人以上"};
        // map暂存信息
        Map<String, Integer> linkMap = new LinkedHashMap<>();
        for (String companySize : companySizes) {
            linkMap.put(companySize, 0);
        }
        for (Map<String, String> map : sizeList) {
            String companySize = map.get("companySize");
            if (linkMap.containsKey(companySize)) {
                linkMap.put(companySize, linkMap.get(companySize) + 1);
            }
        }
        ArrayList<JSONObject> list = new ArrayList<>();
        for (String s : linkMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("value", linkMap.get(s));
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 根据条件获取学历相关信息
     *
     * @param region   地区
     * @param level    地区级别
     * @param position 职位
     * @return
     */
    @Override
    public List<JSONObject> getEduInfo(String region, String level, String position) throws DaoException {
        List<Map<String, String>> eduList = jobAnalyzeDao.getEduInfo(region, level, position);
        // 固定参数
        String[] edus = {"不限", "大专", "本科", "硕士", "博士"};
        // 统计至map集合
        Map<String, Integer> linkMap = new LinkedHashMap<>();
        for (String s : edus) {
            linkMap.put(s, 0);
        }
        for (Map<String, String> map : eduList) {
            String education = map.get("education");
            if (linkMap.containsKey(education)) {
                linkMap.put(education, linkMap.get(education) + 1);
            }
        }
        ArrayList<JSONObject> list = new ArrayList<>();
        for (String s : linkMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("value", linkMap.get(s));
            jsonObject.put("const", "const");
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 根据条件获取公司福利情况
     *
     * @param region   地区
     * @param level    地区级别
     * @param position 职位
     * @return
     * @throws DaoException
     */
    @Override
    public List<JSONObject> getCompanyWelfare(String region, String level, String position) throws DaoException {
        List<Map<String, String>> welfareList = jobAnalyzeDao.getCompanyWelfare(region, level, position);
        System.out.println(welfareList);
        Map<String, Integer> linkMap = new LinkedHashMap<>();
        for (Map<String, String> map : welfareList) {
            String companyLabel = map.get("companyLabelList");
            JSONArray objects = JSONArray.parseArray(companyLabel);
            if (objects == null) {
                continue;
            }
            List<String> welfares = objects.toJavaList(String.class);
            for (String welfare : welfares) {
                if (linkMap.containsKey(welfare)) {
                    linkMap.put(welfare, linkMap.get(welfare) + 1);
                } else {
                    linkMap.put(welfare, 1);
                }
            }
        }
        linkMap = DataService.getValueSortMap(linkMap, 0);
        List<JSONObject> list = new ArrayList<>();
        for (String s : linkMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("value", linkMap.get(s));
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public List<JSONObject> getCompanyFinance(String region, String level, String position) throws DaoException {
        List<Map<String, String>> financeList = jobAnalyzeDao.getCompanyFinance(region, level, position);

        // 整合至map
        Map<String, Integer> linkMap = new LinkedHashMap<>();
        for (Map<String, String> map : financeList) {
            String finanaceStage = map.get("financeStage");
            // 过滤劣质数据
            if (finanaceStage.equals("") | finanaceStage.equals("???")) {
                continue;
            }
            if (linkMap.containsKey(finanaceStage)) {
                linkMap.put(finanaceStage, linkMap.get(finanaceStage) + 1);
            } else {
                linkMap.put(finanaceStage, 1);
            }
        }
        // 返回结果封装
        List<JSONObject> list = new ArrayList<>();
        for (String s : linkMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("value", linkMap.get(s));
            jsonObject.put("const", "const");
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 获取 工作经验-学历-薪资的关系
     *
     * @return
     */
    @Override
    public JSONObject getCountryExpScalEdu() throws DaoException {
        List<Map<String, String>> list = jobAnalyzeDao.getCountryExpScalEdu();
        JSONObject resultJson = new JSONObject();
        String[] workYears = {"不限", "应届毕业生", "1年以下", "1-3年", "3-5年", "5-10年", "10年以上"};


        Map<String, Double> all = new LinkedHashMap<>();
        Map<String, Integer> allNum = new LinkedHashMap<>();
        Map<String, Integer> jcc = new LinkedHashMap<>();
        Map<String, Integer> rcc = new LinkedHashMap<>();
        for (String workYear : workYears) {
            all.put(workYear, 0.0);
            allNum.put(workYear, 0);
            jcc.put(workYear, 0);
            rcc.put(workYear, 0);
        }

        for (Map<String, String> map : list) {
            String education = map.get("education");
            String workYear = map.get("workYear");
            String salary = map.get("salary");
            double salary_d = DataService.getSalaryDouble(salary);
            if (all.containsKey(workYear)) {
                all.put(workYear, all.get(workYear) + salary_d);
                allNum.put(workYear, allNum.get(workYear) + 1);
            }
            if (education.equals("不限") | education.equals("大专")) {
                jcc.put(workYear, jcc.get(workYear) + 1);
            }
            if (education.equals("不限") | (!education.equals("大专") & !education.equals(""))) {
                if (workYear != null && !workYear.equals(""))
                    rcc.put(workYear, rcc.get(workYear) + 1);
            }
        }
        ArrayList<JSONObject> allListJson = new ArrayList<>();
        ArrayList<JSONObject> jccListJson = new ArrayList<>();
        ArrayList<JSONObject> rccListJson = new ArrayList<>();
        for (String key : all.keySet()) {
            // 全部数据对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", key);
            jsonObject.put("value", ((int) (all.get(key) / allNum.get(key))) + "K");
            allListJson.add(jsonObject);
            // 大专数据对象
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name", key);
            jsonObject1.put("value", jcc.get(key));
            jccListJson.add(jsonObject1);
            // 本科及以上数据对象
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("name", key);
            jsonObject2.put("value", rcc.get(key));
            rccListJson.add(jsonObject2);
        }
        resultJson.put("salary", allListJson);
        resultJson.put("jcc", jccListJson);
        resultJson.put("rcc", rccListJson);
        return resultJson;
    }


}
