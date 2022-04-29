package com.yisen.hbaseboot;

import com.alibaba.fastjson.JSONArray;
import com.csvreader.CsvReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvMain {

    public static void main(String[] args) throws IOException {
//        String filePath = "/home/hadoop/下载/ceshi/hbase-boot/src/test/java/com/yisen/hbaseboot/new.csv";
        String filePath = "/usr/local/coderush2022/data/data(1).csv";

        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
        String[] lie = {"positionName", "positionId", "companyId", "salary", "city", "city_province", "district", "workYear", "education", "jobNature", "positionLables", "companyLabelList", "skillLables", "companyFullName", "companyShortName", "famousCompany", "industryField", "financeStage", "companySize", "companyLogo", "longitude", "latitude", "firstType", "secondType", "thirdType", "createTime", "positionDesc"};
        // 读表头
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
            String positionDesc = csvReader.get("positionDesc");
            if (positionDesc.equals("") || positionDesc.length() <= 4) {
                continue;
            }
            positionDesc = positionDesc.substring(2, positionDesc.length() - 2);
            String[] split = positionDesc.split("\",\"");
            List<String> list = new ArrayList<>();
            for (String str : split) {
                str = str.trim();
                if (!str.equals("")) {
                    list.add(str);
                }
            }
            JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(list));
            System.out.println(jsonArray);
        }
    }
}
