package com.yisen.hbaseboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.yisen.hbaseboot.dao.IfDao;
import com.yisen.hbaseboot.dao.PostDao;
import com.yisen.hbaseboot.mapper.PostMapper;
import com.yisen.hbaseboot.mapper.SelectMapper;
import com.yisen.hbaseboot.service.PostService;
import com.yisen.hbaseboot.util.Atlas;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/5 14:57
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostDao postDao;
    @Autowired
    IfDao ifDao;
    //    @Autowired
//    PostMapper postMapper;
    @Autowired
    PostMapper postMapper;

    @Override
    public void postSingleRecord(Map<String, String> map) throws DaoException, ServiceException {
        String positionDesc = map.get("positionDesc");
        if (map.keySet().size() != 24 || positionDesc.indexOf("<") >= 0 || positionDesc.indexOf(">") >= 0 || positionDesc.indexOf("/") >= 0) {
            throw new ServiceException("数据不完整、不存储", 400);
        }
        if (ifDao.ifPositionId(map.get("positionId")) != 0) {
            throw new ServiceException("该positionId已存在", 400);
        }
        map.put("row", map.get("positionId"));
        postDao.postSingleRecord(map);
    }

    @Override
    public void importData() throws IOException, DaoException {
        //String filePath = "/home/hadoop/下载/ceshi/hbase-boot/src/test/java/com/yisen/hbaseboot/new.csv";
//        String filePath = "E:\\CodeRush\\hbase-boot-master\\src\\test\\java\\com\\yisen\\hbaseboot\\new.csv";
        String filePath = "/usr/local/coderush2022/data/data(1).csv";

        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
        String[] lie = {"positionName", "positionId", "companyId", "salary", "city", "city_province", "district", "workYear", "education", "jobNature", "positionLables", "companyLabelList", "skillLables", "companyFullName", "companyShortName", "famousCompany", "industryField", "financeStage", "companySize", "companyLogo", "longitude", "latitude", "firstType", "secondType", "thirdType", "createTime", "positionDesc"};
//        System.out.println(lie.length);
        // 读表头
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (String s : lie) {
                if (s.equals("positionDesc")) { // 职位描述处理
                    String positionDesc = csvReader.get("positionDesc");
                    if (positionDesc.equals("") || positionDesc.length() <= 4) {
                        map.put(s, positionDesc);
                        continue;
                    }
                    positionDesc = positionDesc.substring(2, positionDesc.length() - 2);
                    String[] split = positionDesc.split("\",\"");
                    List<String> list = new ArrayList<>();
                    //for(String str:split){
                    //    str = str.trim();
                    //    if(!str.equals("")){
                    //        list.add(str);
                    //    }
                    //}
                    for (int i = 0; i < split.length; i++) {
                        String str = split[i];
                        str = str.trim();
                        if (str.equals("") && i == 0) {
                            continue;
                        }
                        list.add(str);
                    }
                    String str = JSONArray.toJSONString(list);
                    map.put(s, str);
                } else {
                    map.put(s, csvReader.get(s));
                }
            }
            map.put("row", map.get("positionId"));

            // 时间字段处理
            String createTime = map.get("createTime");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
            LocalDate localDate = LocalDate.parse(createTime, dateTimeFormatter);
            DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy/M/d");
            String format = dateTimeFormatter1.format(localDate);
            map.put("createTime", format);
            // 工资左端和右端
            String salary = map.get("salary");
            String leftSalary = "";
            String rightSalary = "";
            if (salary.contains("k以上")) {
                leftSalary = salary.substring(0, salary.indexOf("k"));
                rightSalary = "0";
            } else {
                leftSalary = salary.substring(0, salary.indexOf("k") != -1 ? salary.indexOf("k") : salary.indexOf("K"));
                rightSalary = salary.substring(salary.indexOf("-") + 1, salary.length() - 1);
            }
            if (map.get("city").equals("海外"))
                continue;
            if (map.get("city_province") != null && !map.get("city_province").equals("")) {
                // 数据插入hbase
                postDao.postSingleRecord(map);
                map.put("leftSalary", leftSalary);
                map.put("rightSalary", rightSalary);
                // 数据插入mysql
                postMapper.addSinglePosition(map);
            } else {
//                System.out.println(map);
                continue;
            }

        }
    }

//    @Override
//    public void importData() throws IOException, DaoException {
//        InputStreamReader input = new InputStreamReader(new FileInputStream("D:\\eclipse\\hbase_boot\\src\\test\\java\\com\\yisen\\hbaseboot\\pos_detail.csv"));
//        BufferedReader br = new BufferedReader(input);
//        String line = br.readLine();
//        int n = 0;
////        String line = "";
//        while (true){
////            line = br.readLine();
////            if (line!=null){
////                if (line.equals(""))
////                    continue;
////                Map<String, String> map = new LinkedHashMap<>();
////                line = line.replace("\', \'","\'、\'");
////                line = line.replace("\"","");
////                String[] split = line.split(",");
////                if (split.length!=57){
////                    n++;
////                    System.out.println(split.length);
////                    continue;
////                }
//                String[] lie = {"mirai","positionId","positionName","companyId","companyFullName","companyShortName","companyLogo","companySize","industryField","financeStage","companyLabelList","firstType","secondType","thirdType","skillLables","positionLables","industryLables","createTime","formatCreateTime","city","district","businessZones","salary","workYear","jobNature","education","positionAdvantage","imState","lastLogin","publisherId","approve","subwayline","stationname","linestaion","latitude","longitude","hitags","resumeProcessRate","resumeProcessDay","score","newScore","matchScore","matchScoreExplain","query","explain","isSchoolJob","adWord","plus","pcShow","appShow","deliver","gradeDescription","promotionScoreExplain","isHotHire","count","aggregatePositionIds","famousCompany"};
//                for (int i = 0; i < split.length; i++) {
//                    map.put(lie[i],split[i]);
//                }
//                map.put("row", map.get("positionId"));
//                // 公司福利字段处理
//                String companyLabelList = map.get("companyLabelList");
//                companyLabelList = companyLabelList.replace("、", ",");
//                map.put("companyLabelList", companyLabelList);
//                // 时间字段处理
//                String createTime = map.get("createTime");
//                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                TemporalAccessor parse = dateTimeFormatter.parse(createTime);
//                LocalDateTime localDateTime = LocalDateTime.from(parse);
//                map.put("createTime",localDateTime.getYear()+"/"+localDateTime.getMonthValue()+"/"+localDateTime.getDayOfMonth());
//                // 省份字段处理
//                String city = map.get("city").substring(0, 2);
//                JSONObject atlas = Atlas.getAtlas();
//                for (Object province : atlas.getJSONArray("provinces")) {
//                    JSONObject jsonObject = JSONObject.parseObject(province.toString());
//                    JSONArray citys = jsonObject.getJSONArray("citys");
//                    boolean flag = false;
//                    for (Object o : citys) {
//                        JSONObject object = JSONObject.parseObject(o.toString());
//                        if (object.getString("citysName").contains(city)) {
//                            map.put("city_province", jsonObject.getString("provinceName").substring(0, 2));
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (flag) {
//                        break;
//                    }
//                }
////                postDao.postSingleRecord(map);
//            }else{
//                break;
//            }
//        }
//        System.out.println(n);
//    }

}
