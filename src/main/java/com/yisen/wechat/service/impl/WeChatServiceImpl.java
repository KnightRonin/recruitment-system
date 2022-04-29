package com.yisen.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.wechat.dao.WeChatDao;
import com.yisen.wechat.mapper.WeChatSelectMapper;
import com.yisen.wechat.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/8/4 10:34
 */
@Service
public class WeChatServiceImpl implements WeChatService {
    @Autowired
    WeChatDao weChatDao;
    @SuppressWarnings("all")
    @Autowired
    WeChatSelectMapper weChatSelectMapper;

    @Override
    public JSONArray getCompare() throws DaoException {
        Set<String> citys = new HashSet<String>(Arrays.asList(new String[]{"北京", "上海", "广州", "深圳", "成都", "重庆", "杭州", "武汉", "西安", "天津", "苏州", "南京", "郑州", "长沙", "东莞", "沈阳", "青岛", "合肥", "佛山"}));
        int today = 1500;
        int yesterday = 879;
        JSONArray jsonArray = new JSONArray();
//        LocalDate localDate = LocalDate.now();
        LocalDate localDate = LocalDate.of(2022, 3, 1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        String date1 = dateTimeFormatter.format(localDate);
        List<Map<String, String>> list1 = weChatDao.getCompare(date1);
        localDate = localDate.plusDays(-1);
        String date = dateTimeFormatter.format(localDate);
        List<Map<String, String>> list = weChatDao.getCompare(date);
        // 返回属性 recruitmentTotal
        today = list1.size();
        yesterday = list.size();
        JSONObject recruitmentTotal = new JSONObject();
        recruitmentTotal.put("name", "招聘数量");
        recruitmentTotal.put("today", today);
        recruitmentTotal.put("yesterday", yesterday);
        jsonArray.add(recruitmentTotal);


        // positionTotal、companyTotal、cityTotal、practiceTotal、firstCityTotal
        HashSet<String> position = new HashSet<>();// 不同职位数量
        JSONObject positionTotal = new JSONObject();
        positionTotal.put("name", "岗位数量");
        HashSet<String> company = new HashSet<>();// 不同公司数量
        JSONObject companyTotal = new JSONObject();
        companyTotal.put("name", "企业数量");
        HashSet<String> city = new HashSet<>();// 不同城市数量
        JSONObject cityTotal = new JSONObject();
        cityTotal.put("name", "城市数量");
        int jobNature = 345;// 应届生招聘数量
        JSONObject practiceTotal = new JSONObject();
        practiceTotal.put("name", "城市数量");
        int firstCity = 124;// 所有一线城市招聘数量
        JSONObject firstCityTotal = new JSONObject();
        firstCityTotal.put("name", "一线城市");
        for (Map<String, String> map : list) {
            String city1 = map.get("city");
            position.add(map.get("secondType"));
            company.add(map.get("companyFullName"));
            city.add(city1);
            if (map.get("jobNature").equals("实习")) {
                jobNature++;
            }
            if (citys.contains(city1)) {
                firstCity++;
            }
        }
        positionTotal.put("yesterday", position.size());
        companyTotal.put("yesterday", company.size());
        cityTotal.put("yesterday", city.size());
        practiceTotal.put("yesterday", jobNature);
        firstCityTotal.put("yesterday", firstCity);
        position.clear();
        company.clear();
        city.clear();
        jobNature = 456;
        firstCity = 13;
        for (Map<String, String> map : list1) {
            String city1 = map.get("city");
            position.add(map.get("secondType"));
            company.add(map.get("companyFullName"));
            city.add(city1);
            if (map.get("jobNature").equals("实习")) {
                jobNature++;
            }
            if (citys.contains(city1)) {
                firstCity++;
            }
        }

        positionTotal.put("today", position.size());// 不同职位数量的添加
        jsonArray.add(positionTotal);
        companyTotal.put("today", company.size());// 不同公司数量的添加
        jsonArray.add(companyTotal);
        cityTotal.put("today", city.size());// 不同城市的数量
        jsonArray.add(cityTotal);
        practiceTotal.put("today", jobNature);// 实习生岗位数量
        jsonArray.add(practiceTotal);
        firstCityTotal.put("today", firstCity);// 所有一线城市招聘数量
        jsonArray.add(firstCityTotal);

        return jsonArray;
    }

    /**
     * 返回符合条件的前 5条数据
     *
     * @param salary
     * @param city
     * @param pos
     * @param jobNature
     * @param positionLables
     * @param extraId
     * @return
     */
    @Override
    public JSONArray matchPos(String salary, String city, String pos, String jobNature, String positionLables, String extraId) {
        // 返回结果
        JSONArray jsonArray = new JSONArray();
        // 工资区间处理
        String leftSalary = "";
        String rightSalary = "";
        if (salary.contains("k以上")) {
            leftSalary = salary.substring(0, salary.indexOf("k"));
            rightSalary = "0";
        } else {
//            System.out.println(salary);
            leftSalary = salary.substring(0, salary.indexOf("k") != -1 ? salary.indexOf("k") : salary.indexOf("K"));
            rightSalary = salary.substring(salary.indexOf("-") + 1, salary.length() - 1);
        }
        // 城市处理
        List<String> citys = JSONArray.parseArray(city).toJavaList(String.class);
        // 职位处理
        List<String> position = JSONArray.parseArray(pos).toJavaList(String.class);
        List<Map<String, String>> positions = new ArrayList<Map<String, String>>();
        for (String s : position) {
            String[] split = s.split("-");
            HashMap<String, String> map = new HashMap<>();
            map.put("secondType", split[0]);
            map.put("thirdType", split[1]);
            positions.add(map);
        }
        // 职位标签处理
        List<String> lables = JSONArray.parseArray(positionLables).toJavaList(String.class);
        // 排除的职位ID处理
//        System.out.println(extraId);
        List<String> extraIds = JSONArray.parseArray(extraId).toJavaList(String.class);

        List<Map<String, String>> list = weChatSelectMapper.matchPos(leftSalary, rightSalary, citys, positions, lables, extraIds, jobNature);
        for (Map<String, String> map : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("positionId", map.get("positionId"));
            jsonObject.put("positionName", map.get("positionName"));
            jsonObject.put("companyShortName", map.get("companyShortName"));
            jsonObject.put("companyLogo", map.get("companyLogo"));
            jsonObject.put("city", map.get("city"));
            jsonObject.put("workYear", map.get("workYear"));
            jsonObject.put("education", map.get("education"));
            jsonObject.put("jobNature", map.get("jobNature"));
            jsonObject.put("salary", map.get("salary"));
            jsonObject.put("industryField", map.get("industryField"));
            jsonArray.add(jsonObject);

        }

        return jsonArray;
    }

    /**
     * 返回最近一个月 招聘数量最多的前三种职位，返回每日数量。
     *
     * @return
     */
    @Override
    public JSONArray posTrend(String city) throws DaoException {
        JSONArray jsonArray = new JSONArray();
        ArrayList<String> date = new ArrayList<>();
        // 取得要查询的日期
//        LocalDate localDate = LocalDate.now();
//        System.out.println("当前日期"+localDate);
        LocalDate localDate = LocalDate.of(2022, 3, 1);
        localDate.plusDays(1);// 这里加一是和第一次循环会先减一抵消
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        for (int i = 0; i < 30; i++) { // 拿到最近几天日期
            localDate = localDate.plusDays(-1);
            date.add(dateTimeFormatter.format(localDate));
        }
        // dao 获取数据
        List<Map<String, String>> list = weChatDao.posTrend(date, city);

        // 根据职位进行分类
        Map<String, Integer> thirdTypeMap = DataService.columnClassSum(list, "thirdType");
        // 进行排序 拿到前三个职位类型
        thirdTypeMap = DataService.getValueSortMap(thirdTypeMap, 3);
        System.out.println(thirdTypeMap);
        ArrayList<String> arrayList = new ArrayList<>(thirdTypeMap.keySet());

        // 进行前三清洗
        List<Map<String, String>> list1 = new ArrayList<>();// 排名第一
        List<Map<String, String>> list2 = new ArrayList<>();// 排名第二
        List<Map<String, String>> list3 = new ArrayList<>();// 排名第三
        for (Map<String, String> map : list) {
            String thirdType = map.get("thirdType");
            int i = arrayList.indexOf(thirdType);
            if (i == 0) {
                list1.add(map);
            } else if (i == 1) {
                list2.add(map);
            } else if (i == 2) {
                list3.add(map);
            }
        }
        // 对不同职位进行时间分类统计
        Map<String, Integer> createTime1 = DataService.columnClassSum(list1, "createTime");
        Map<String, Integer> createTime2 = DataService.columnClassSum(list2, "createTime");
        Map<String, Integer> createTime3 = DataService.columnClassSum(list3, "createTime");
        // 将数据格式转换为前端需求格式
//        JSONArray jsonArray1 = new JSONArray();
//        JSONArray jsonArray2 = new JSONArray();
//        JSONArray jsonArray3 = new JSONArray();
        // thirdType 记录
        String thirdType1 = list1.get(0).get("thirdType");
        String thirdType2 = list2.get(0).get("thirdType");
        String thirdType3 = list3.get(0).get("thirdType");
        for (String time : date) {
            // 前端要的时间格式
            String t = time.substring(time.indexOf("/") + 1).replace("/", "-");
            // 三个属性 date、type 和 value。是返回最内层数据，对应排名第一、二、三
            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            JSONObject jsonObject3 = new JSONObject();
            // 三个属性赋值
            jsonObject1.put("date", t);
            jsonObject2.put("date", t);
            jsonObject3.put("date", t);
            jsonObject1.put("value", createTime1.get(time) == null ? 0 : createTime1.get(time));
            jsonObject2.put("value", createTime2.get(time) == null ? 0 : createTime2.get(time));
            jsonObject3.put("value", createTime3.get(time) == null ? 0 : createTime3.get(time));
            jsonObject1.put("type", thirdType1);
            jsonObject2.put("type", thirdType2);
            jsonObject3.put("type", thirdType3);
            jsonArray.add(jsonObject1);
            jsonArray.add(jsonObject2);
            jsonArray.add(jsonObject3);
        }
//        jsonObject.put(thirdType2, jsonArray2);
//        jsonObject.put(thirdType3, jsonArray3);
        return jsonArray;
    }

    /**
     * 全国薪资区域平均 [3,7,10,15,18]
     *
     * @return
     */
    @Override
    public JSONArray countrySalary() throws DaoException {
        JSONArray result = new JSONArray();
        List<Map<String, String>> list = weChatDao.countrySalary();
        ArrayList<Integer> arrayList = new ArrayList<>();
        list.stream().map(m -> {
            String salary = m.get("salary");
            if (salary.contains("k")) {
                salary = salary.substring(salary.indexOf("-") + 1, salary.indexOf("k", salary.indexOf("-") + 1));
            } else if (salary.contains("K")) {
                salary = salary.substring(salary.indexOf("-") + 1, salary.indexOf("K", salary.indexOf("-") + 1));
            }
            //m.put("salary", salary);
            arrayList.add(Integer.parseInt(salary));
            return m;
        }).collect(Collectors.toList());
        arrayList.sort((a, b) -> a - b);
        int n = arrayList.size() / 5;
        int i = 1;
        int[] sum = new int[6];
        for (int j = 0; j < arrayList.size(); j++) {
            if (j <= i * n || i == 5) {
                sum[i] += arrayList.get(j);
            } else {
                i++;
                sum[i] = arrayList.get(j);
            }
        }
        BigDecimal bigDecimal = new BigDecimal(0.00);
        for (int j = 1; j < 5; j++) {
            bigDecimal = new BigDecimal(sum[j] / (n * 1.0));
            result.add(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        int cha = arrayList.size() - (4 * n);
        bigDecimal = new BigDecimal(sum[5] / (cha * 1.0));
        result.add(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return result;
    }
}
