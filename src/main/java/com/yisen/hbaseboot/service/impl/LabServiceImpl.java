package com.yisen.hbaseboot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.dao.LabDao;
import com.yisen.hbaseboot.entity.TableData;
import com.yisen.hbaseboot.service.LabService;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/12 16:43
 */
@Service
public class LabServiceImpl implements LabService {
    @Autowired
    LabDao labDao;
    @Autowired
    String family;

    /**
     * 返回表格所需数据
     *
     * @param jsonObject
     * @return
     */
    @Override
    public List<List<TableData>> getTableEnd(JSONObject jsonObject) throws DaoException, ServiceException {
        // 此类为决定Map<Key,TableData> key唯一存在
        class Key {
            String date;
            String location;
            String position;

            public Key(String date, String location, String position) {
                this.date = date;
                this.location = location;
                this.position = position;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            @Override
            public String toString() {
                return "Key{" +
                        "date='" + date + '\'' +
                        ", location='" + location + '\'' +
                        ", position='" + position + '\'' +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Key key = (Key) o;
                return date.equals(key.date) &&
                        location.equals(key.location) &&
                        Objects.equals(position, key.position);
            }

            @Override
            public int hashCode() {
                return Objects.hash(date, location, position);
            }
        }
        // hbase 中 省字段的值
//        String[] sheng = {"山东", "福建", "台湾", "河南", "河北", "重庆", "湖南", "湖北",
//                "江西", "海南", "黑龙江", "天津", "陕西", "贵州", "新疆", "澳门", "江苏",
//                "安徽", "西藏", "上海", "吉林", "山西", "甘肃", "香港", "宁夏", "四川", "浙江",
//                "广西", "云南", "海外", "内蒙古", "辽宁", "广东", "青海", "北京"};
        String sheng = "[山东, 福建, 台湾, 河南, 河北, 重庆, 湖南, 湖北, 江西, 海南, 黑龙江, 天津, 陕西, 贵州, 新疆, 澳门, 江苏, 安徽, 西藏, 上海, 吉林, 山西, 甘肃, 香港, 宁夏, 四川, 浙江, 广西, 云南, 海外, 内蒙古, 辽宁, 广东, 青海, 北京]";
        // 所有查询条件
        Map<String, List<String>> map = new HashMap<>();
        // 生成时间限制条件
        JSONArray time = jsonObject.getJSONArray("time");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d");
        List<String> times = new ArrayList<String>();
        for (int i = 0; i < time.size(); i++) {
            String timeStr = time.getString(i).replace("-", "/");
            try {
                timeStr = simpleDateFormat.format(simpleDateFormat.parse(timeStr));
            } catch (ParseException e) {
                throw new ServiceException("日期格式错误");
            }
            times.add(timeStr);
        }
        // 生成地区限制条件
        List<String> location_province = new ArrayList<>();
        List<String> location_city = new ArrayList<>();
        // 获得地区参数
        JSONObject locationJson = jsonObject.getJSONObject("location");
        JSONArray level1 = locationJson.getJSONArray("level1");
        JSONArray level2 = locationJson.getJSONArray("level2");
        JSONArray level3 = locationJson.getJSONArray("level3");
        String level1Str = level1.toString();
        String level2Str = level2.toString();
        String level3Str = level3.toString();
        // 得到请求的省市
        for (int i = 0; i < level2.size(); i++) {
            location_province.add(level2.getString(i).substring(0, 2));
        }
        for (int i = 0; i < level3.size(); i++) {
            location_city.add(level3.getString(i).substring(0, 2));
        }
        // 添加查询列与条件
        map.put("createTime", times); //时间条件
        map.put("salary", null); // 工资
        // 决定查询地区列
        if (level1.size() == 0) {// 确定不是全国
            if (level2.size() != 0) {
                map.put("city_province", location_province);// 省
                map.put("city", null);// 市
            }
            if (level3.size() != 0) {
                map.put("city", location_city);// 市
                map.put("district", null);// 区
            }

        } else {// 统计全国
            map.put("city_province", null);// 省
            if (level2.size() != 0) {
                map.put("city", null);// 市
            }
            if (level3.size() != 0) {
                map.put("district", null);// 区
            }
        }
        map.put("companySize", null);// 公司规模
        map.put("companyLabelList", null);// 公司福利
        map.put("secondType", null);// 职位名称
        List<Map<String, String>> tableEnd = labDao.getTableEnd(family, map);
        // ==============================================================================================================
        // list<Map<String,String>> 封装为 List<List<TableData>>
        // 返回结果
        List<List<TableData>> resultList = new ArrayList<List<TableData>>();
        // 需要具体统计的几个职位(要统计区域内所有职位的数量所以dao不加过滤器)
        String position = jsonObject.getString("position");
        List<String> positions = Arrays.asList(position.split("、"));
        // 需要几个List<TableData>
        int listSize = times.size();
        // 每个List<TableData>需要几个TableData
        List<String> tabList = level1.toJavaList(String.class);
        int levelSize1 = level1.size();
        int levelSize2 = level2.size();
        int levelSize3 = level3.size();
        int locationSize = levelSize1 + levelSize2 + levelSize3;
        int positionSize = positions.size();
        // ===========================================================================================================================
        // 数据统计类型制定
        System.out.println(map);
        //----location----
        Map<Key, Integer> locationNumMap = new LinkedHashMap<>();// 存不同locationNum
        Map<Key, Double> locationSalaryMap = new LinkedHashMap<>();// 不同的locationSalary
        Map<Key, Map> locationHotLocation = new LinkedHashMap<>();// 存不同的下级locationHotLocation
        Map<Key, Map> locationCompanySize = new LinkedHashMap<>();// 存不同的公司规模locationCompanySize
        Map<Key, Map> locationBenefit = new LinkedHashMap<>(); // 存不同的公司福利
        //----position----
        Map<Key, Integer> positionNum = new LinkedHashMap<>();
        Map<Key, Double> positionSalary = new LinkedHashMap<>();
        Map<Key, Map> positionHotLocation = new LinkedHashMap<>();
        Map<Key, Map> positionCompanySize = new LinkedHashMap<>();// 存不同的公司规模positionCompanySize
        Map<Key, Map> positionBenefit = new LinkedHashMap<>(); // 存不同的公司福利
        for (String createTime : map.get("createTime")) {
            if (!level1Str.equals("[]")) {
                Key key = new Key(createTime, "中国", null);
                locationNumMap.put(key, 0);
                locationSalaryMap.put(key, 0.0);
                locationHotLocation.put(key, new LinkedHashMap<String, Integer>());
                locationCompanySize.put(key, new LinkedHashMap<String, Integer>());
                locationBenefit.put(key, new LinkedHashMap<String, Integer>());
                for (String s : positions) {
                    Key key1 = new Key(createTime, "中国", s);
                    positionNum.put(key1, 0);
                    positionSalary.put(key1, 0.0);
                    positionHotLocation.put(key1, new LinkedHashMap<String, Integer>());
                    positionCompanySize.put(key1, new LinkedHashMap<String, Integer>());
                    positionBenefit.put(key1, new LinkedHashMap<String, Integer>());
                }
            }
            if (!level2Str.equals("[]")) {
                for (String province : location_province) {
                    Key key = new Key(createTime, province, null);
                    locationNumMap.put(key, 0);
                    locationSalaryMap.put(key, 0.0);
                    locationHotLocation.put(key, new LinkedHashMap<String, Integer>());
                    locationCompanySize.put(key, new LinkedHashMap<String, Integer>());
                    locationBenefit.put(key, new LinkedHashMap<String, Integer>());
                    for (String s : positions) {
                        Key key1 = new Key(createTime, province, s);
                        positionNum.put(key1, 0);
                        positionSalary.put(key1, 0.0);
                        positionHotLocation.put(key1, new LinkedHashMap<String, Integer>());
                        positionCompanySize.put(key1, new LinkedHashMap<String, Integer>());
                        positionBenefit.put(key1, new LinkedHashMap<String, Integer>());
                    }
                }
            }
            if (!level3Str.equals("[]")) {
                for (String city : location_city) {
                    Key key = new Key(createTime, city, null);
                    locationNumMap.put(key, 0);
                    locationSalaryMap.put(key, 0.0);
                    locationHotLocation.put(key, new LinkedHashMap<String, Integer>());
                    locationCompanySize.put(key, new LinkedHashMap<String, Integer>());
                    locationBenefit.put(key, new LinkedHashMap<String, Integer>());
                    for (String s : positions) {
                        Key key1 = new Key(createTime, city, s);
                        positionNum.put(key1, 0);
                        positionSalary.put(key1, 0.0);
                        positionHotLocation.put(key1, new LinkedHashMap<String, Integer>());
                        positionCompanySize.put(key1, new LinkedHashMap<String, Integer>());
                        positionBenefit.put(key1, new LinkedHashMap<String, Integer>());
                    }
                }
            }
        }
        //=================================================================================================================================
//        Map<String,LinkedHashMap<String, String>> intMap = new LinkedHashMap<>();
        // 遍历dao结果，每一个map 都是 一行数据 数据清洗
        for (Map<String, String> tableMap : tableEnd) {
            String dateKey = tableMap.get("createTime");
            String city_province = tableMap.get("city_province");
            String city = tableMap.get("city");
            String district = tableMap.get("district");
            String secondType = tableMap.get("secondType");
            String companySize = tableMap.get("companySize");
            String labelList = tableMap.get("companyLabelList");
            String salary = tableMap.get("salary"); // 15k-30k
            double salary_d = 0;
            if (salary.indexOf("k") == -1) {
                salary_d = Double.parseDouble(salary.substring(0, salary.indexOf("K")));
            } else {
                salary_d = Double.parseDouble(salary.substring(0, salary.indexOf("k")));
            }

            List<String> labList = JSONArray.parseArray(labelList).toJavaList(String.class);
            List<Key> keyls = new ArrayList<>();// 该行数据可能使用的key---location
            List<Key> keyps = new ArrayList<>();// 该行数据可能使用的key---position
            if (!level1Str.equals("[]")) {
                keyls.add(new Key(dateKey, "中国", null));
                keyps.add(new Key(dateKey, "中国", secondType));
            }
            keyls.add(new Key(dateKey, city_province, null));
            keyls.add(new Key(dateKey, city, null));
            keyps.add(new Key(dateKey, city_province, secondType));
            keyps.add(new Key(dateKey, city, secondType));
            for (Key keyl : keyls) {
                // 地区职位数量
                if (locationNumMap.containsKey(keyl)) {
                    locationNumMap.put(keyl, locationNumMap.get(keyl) + 1);
                }
                // 地区工资统计
                if (locationSalaryMap.containsKey(keyl)) {
                    locationSalaryMap.put(keyl, locationSalaryMap.get(keyl) + salary_d);
                }
                // 下级城市职位数量统计
                if (locationHotLocation.containsKey(keyl)) {
                    Map<String, Integer> map1 = locationHotLocation.get(keyl);
                    if (keyl.getLocation().equals("中国")) {
                        if (map1.containsKey(city_province)) {
                            map1.put(city_province, map1.get(city_province) + 1);
                        } else {
                            map1.put(city_province, 1);
                        }
                    } else if (sheng.contains(keyl.getLocation())) {
                        if (map1.containsKey(city)) {
                            map1.put(city, map1.get(city) + 1);
                        } else {
                            map1.put(city, 1);
                        }
                    } else {
                        if (map1.containsKey(district)) {
                            map1.put(district, map1.get(district) + 1);
                        } else {
                            map1.put(district, 1);
                        }
                    }
                }
                // 公司规模职位数量统计
                if (locationCompanySize.containsKey(keyl)) {
                    Map<String, Integer> map1 = locationCompanySize.get(keyl);
                    if (map1.containsKey(companySize)) {
                        map1.put(companySize, map1.get(companySize) + 1);
                    } else {
                        map1.put(companySize, 1);
                    }
                }
                // 公司福利数量统计
                if (locationBenefit.containsKey(keyl)) {
                    Map<String, Integer> map1 = locationBenefit.get(keyl);
                    for (String s : labList) {
                        if (map1.containsKey(s)) {
                            map1.put(s, map1.get(s) + 1);
                        } else {
                            map1.put(s, 1);
                        }
                    }
                }
            }

            for (Key keyp : keyps) {
                // 职位数量统计
                if (positionNum.containsKey(keyp)) {
                    positionNum.put(keyp, positionNum.get(keyp) + 1);
                }
                // 职位工资统计
                if (positionSalary.containsKey(keyp)) {
                    positionSalary.put(keyp, positionSalary.get(keyp) + salary_d);
                }
                // 职位下级城市职位数量统计
                if (positionHotLocation.containsKey(keyp)) {
                    Map<String, Integer> map1 = positionHotLocation.get(keyp);
                    if (keyp.getLocation().equals("中国")) {
                        if (map1.containsKey(city_province)) {
                            map1.put(city_province, map1.get(city_province) + 1);
                        } else {
                            map1.put(city_province, 1);
                        }
                    } else if (sheng.contains(keyp.getLocation())) {
                        if (map1.containsKey(city)) {
                            map1.put(city, map1.get(city) + 1);
                        } else {
                            map1.put(city, 1);
                        }
                    } else {
                        if (map1.containsKey(district)) {
                            map1.put(district, map1.get(district) + 1);
                        } else {
                            map1.put(district, 1);
                        }
                    }
                }
                // 公司规模职位数量统计
                if (positionCompanySize.containsKey(keyp)) {
                    Map<String, Integer> map1 = positionCompanySize.get(keyp);
                    if (map1.containsKey(companySize)) {
                        map1.put(companySize, map1.get(companySize) + 1);
                    } else {
                        map1.put(companySize, 1);
                    }
                }
                // 公司福利数量统计
                if (positionBenefit.containsKey(keyp)) {
                    Map<String, Integer> map1 = positionBenefit.get(keyp);
                    for (String s : labList) {
                        if (map1.containsKey(s)) {
                            map1.put(s, map1.get(s) + 1);
                        } else {
                            map1.put(s, 1);
                        }
                    }
                }
            }

        }
//        System.out.println(locationCompanySize);
        // TableData 结果封装;
        for (int i = 0; i < listSize; i++) { // 时间
            List<TableData> listTable = new ArrayList<>();
            for (int j = 0; j < locationSize; j++) { // 地区
                for (int k = 0; k < positionSize; k++) { // 职位
                    TableData tableData = new TableData();
                    // index
                    tableData.setIndex(i);
                    // time
                    tableData.setDate(times.get(i));
                    String locationName = null;
                    // location
                    if (j < levelSize1) {
                        locationName = level1.getString(j).substring(0, 2);
                        tableData.setLocation(level1.getString(j));
                    } else if (j < levelSize1 + levelSize2) {
                        locationName = level2.getString(j - levelSize1).substring(0, 2);
                        tableData.setLocation(level2.getString(j - levelSize1));
                    } else {
                        locationName = level3.getString(j - levelSize1 - levelSize2).substring(0, 2);
                        tableData.setLocation(level3.getString(j - levelSize1 - levelSize2));
                    }
                    Key keyl = new Key(times.get(i), locationName, null);
                    tableData.setLocationNum(locationNumMap.get(keyl));
                    // 地区平均薪资
                    String locationSalary = String.valueOf((int) (locationSalaryMap.get(keyl) * 1000 / locationNumMap.get(keyl)));
                    tableData.setLocationSalary(locationSalary);
                    // 下级地区前三
                    Map valueSortMap = DataService.getValueSortMap(locationHotLocation.get(keyl), 3);
                    String hotLocation = "";
                    Set<String> set = valueSortMap.keySet();
                    for (String s : set) {
                        hotLocation += s + "、";
                    }
                    if (hotLocation.length() != 0)
                        hotLocation = hotLocation.substring(0, hotLocation.length() - 1);
                    tableData.setLocationHotLocation(hotLocation);
                    // 公司规模最多
                    Map valueSortMap1 = DataService.getValueSortMap(locationCompanySize.get(keyl), 1);
                    tableData.setLocationCompanySize((String) valueSortMap1.keySet().iterator().next());
                    // 福利数量排行前五
                    String welfare = "";
                    Map valueSortMap2 = DataService.getValueSortMap(locationBenefit.get(keyl), 5);
                    Iterator iterator = valueSortMap2.keySet().iterator();
                    while (iterator.hasNext()) {
                        welfare += (String) iterator.next() + "、";
                    }
                    if (welfare.length() != 0)
                        tableData.setLocationBenefit(welfare.substring(0, welfare.length() - 1));
                    // position
                    tableData.setPosition(positions.get(k));
                    listTable.add(tableData);
                    // positionNum 职位数量
                    Key keyp = new Key(times.get(i), locationName, positions.get(k));
                    tableData.setPositionNum(positionNum.get(keyp));
                    // 职位平均工资
                    String pSalary = String.valueOf((int) (positionSalary.get(keyp) * 1000 / positionNum.get(keyp)));
                    tableData.setPositionSalary(pSalary);
                    // 职位下级地区前三
                    Map sortMap = DataService.getValueSortMap(positionHotLocation.get(keyp), 3);
                    String hotLocation1 = "";
                    Set<String> set1 = sortMap.keySet();
                    for (String s : set1) {
                        hotLocation1 += s + "、";
                    }
                    if (hotLocation1.length() != 0)
                        hotLocation1 = hotLocation1.substring(0, hotLocation1.length() - 1);
                    tableData.setPositionHotLocation(hotLocation1);
                    // 职位公司规模
                    Map sortMap1 = DataService.getValueSortMap(positionCompanySize.get(keyp), 1);
                    if (sortMap1.size() != 0) {
                        tableData.setPositionCompanySize((String) sortMap1.keySet().iterator().next());
                    } else {
                        tableData.setPositionCompanySize("");
                    }
                    // 福利数量排行前五
                    String welfare1 = "";
                    Map sortMap2 = DataService.getValueSortMap(positionBenefit.get(keyp), 5);
                    Iterator iterator1 = sortMap2.keySet().iterator();
                    while (iterator1.hasNext()) {
                        welfare1 += (String) iterator1.next() + "、";
                    }
                    if (welfare1.length() != 0)
                        tableData.setPositionBenefit(welfare1.substring(0, welfare1.length() - 1));
                }
            }
            resultList.add(listTable);
        }
        return resultList;
    }

    /**
     * 返回所有时间刻度的数据
     *
     * @param times 所有时间刻度
     * @return
     */
    @Override
    public List<JSONObject> getTimesData(List<String> times) throws DaoException, ParseException {
        ArrayList<String> newTimes = new ArrayList<>();
        Map<String, Map<String, Integer>> resultMap = new LinkedHashMap<>();
        ArrayList<JSONObject> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d");
        for (String time : times) {
            String replace = time.replace("-", "/");
            Date date = simpleDateFormat.parse(replace);
            String format = simpleDateFormat.format(date);
            newTimes.add(format);
        }
        List<String> colList = new ArrayList<>();
        colList.add("createTime");
        colList.add("secondType");
        List<Map<String, String>> data = labDao.getTimesData(family, colList, newTimes);
        for (Map<String, String> map : data) {
            String secondType = map.get("secondType");
            String createTime = map.get("createTime");
            if (resultMap.containsKey(secondType)) {
                Map<String, Integer> map1 = resultMap.get(secondType);
                if (map1.containsKey(createTime)) {
                    map1.put(createTime, map1.get(createTime) + 1);
                } else {
                    map1.put(createTime, 1);
                }
            } else {
                Map<String, Integer> map1 = new LinkedHashMap<>();
                map1.put(createTime, 1);
                resultMap.put(secondType, map1);
            }
        }
        for (String secondType : resultMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            Map<String, Integer> map = resultMap.get(secondType);
            List<Integer> num = new ArrayList<>();
            for (String newTime : newTimes) {
                Integer integer = map.get(newTime);
                num.add(integer);
            }
            jsonObject.put("data", num);
            jsonObject.put("name", secondType);
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 返回指定日期，指定的职位信息
     *
     * @param times
     * @param position
     * @return
     */
    @Override
    public List<JSONObject> getTimePosition(List<String> times, List<String> position) {
        CopyOnWriteArrayList<JSONObject> result = new CopyOnWriteArrayList<>();
        // 时间格式处理
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        times = times.stream().map(obj -> {
            int a = obj.indexOf("-");
            int b = obj.indexOf("-", a + 1);
            int year = Integer.parseInt(obj.substring(0, a));
            int month = Integer.parseInt(obj.substring(a + 1, b));
            int day = Integer.parseInt(obj.substring(b + 1));
            LocalDate localDate = LocalDate.of(year, month, day);
            String format = dateTimeFormatter.format(localDate);
            return format;
        }).collect(Collectors.toList());
        // 拿到dao结果进行封装
        List<Map<String, String>> list = labDao.getTimePosition(times, position);
        // 按照职位分组
        Map<String, List<Map<String, String>>> groupPosition =
                list.stream().filter(obj -> obj.get("secondType") != null)
                        .collect(Collectors.groupingBy(obj -> obj.get("secondType")));
//        System.out.println(groupPosition);
        for (String key : groupPosition.keySet()) {
            List<Map<String, String>> value = groupPosition.get(key);
            CopyOnWriteArrayList<Integer> integers = new CopyOnWriteArrayList<>();
            JSONObject jsonObject = new JSONObject();
            // 封装name
            jsonObject.put("name", key);
            // 封装value
            Map<String, Long> createTime = value.parallelStream().collect(Collectors.groupingBy(obj -> obj.get("createTime"), Collectors.counting()));
            for (String time : times) {
                try {
                    integers.add(Math.toIntExact(createTime.get(time)));
                } catch (NullPointerException e) {
                    integers.add(0);
                }
            }
            jsonObject.put("data", integers);
//            System.out.println(jsonObject);
            result.add(jsonObject);
        }
//        System.out.println(result);
        return result;
    }


}
