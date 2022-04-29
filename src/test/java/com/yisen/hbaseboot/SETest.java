package com.yisen.hbaseboot;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.yisen.hbaseboot.util.Atlas;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @AuthorList: HuangGuoFu
 * @Date: 2022/3/4 17:18
 */
public class SETest {

    //    @Test
//    public void test() throws IOException {
//        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum","192.168.1.39" );
//        conf.set("hbase.client.keyvalue.maxsize","500000");
//        Connection connection = null;
//        try {
//            connection = ConnectionFactory.createConnection(conf);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(connection);
//        Table table = connection.getTable(TableName.valueOf("test_tow"));
//        Get get = new Get(Bytes.toBytes("6778599"));
//        Result result = table.get(get);
//        ResultToString.resultShow(result);

    @Test
    public void test2() throws IOException {
        Atlas atlas = new Atlas();
        JSONObject atlasObject = atlas.getAtlas();
        System.out.println(atlasObject.getJSONArray("provinces").getJSONObject(0).getJSONArray("citys"));
    }

    @Test
    public void data() throws IOException {
        String filePath = "/usr/local/coderush2022/data/data(1).csv";
        String filePath1 = "/usr/local/coderush2022/data/data(1).csv";
        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
        String[] lie = {"positionId", "positionName", "companyId", "salary", "city", "city_province", "district", "workYear", "education", "jobNature", "positionLables", "companyLabelList", "skillLables", "companyFullName", "companyShortName", "famousCompany", "industryField", "financeStage", "companySize", "companyLogo", "longitude", "latitude", "firstType", "secondType", "thirdType", "createTime"};
        // 写出头
        String[] tou = {"positionId", "positionName", "companyId", "companyFullName", "companyShortName", "companyLogo", "companySize", "industryField", "financeStage", "companyLabelList", "firstType", "secondType", "thirdType", "skillLables", "positionLables", "createTime", "city", "district", "salary", "workYear", "jobNature", "education", "latitude", "longitude", "famousCompany", "city_province"};
        //        System.out.println(lie.length);
        CsvWriter csvWriter = new CsvWriter(filePath1, ',', Charset.forName("UTF-8"));
        // 写表头
        csvWriter.writeRecord(tou);
        // 读表头
        csvReader.readHeaders();
//        // 统计每天的数量
//        Map<String, Integer> linkedMap = new LinkedHashMap<>();
//        LocalDate date = LocalDate.of(2020, 3, 1);
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//        for (int i = 0; i < 30; i++) {
//            String format = dateTimeFormatter.format(date);
//            linkedMap.put(format,0);
//            date = date.plusDays(1);
//        }
        // 选择要筛选的日期
        HashSet<String> dates = new HashSet<>();
        LocalDate date = LocalDate.of(2022, 3, 1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        for (int i = 0; i < 29; i++) {
            String format = dateTimeFormatter.format(date);
            dates.add(format);
            date = date.plusDays(1);
        }

        while (csvReader.readRecord()) {
            String createTime = csvReader.get("createTime");
            if (dates.contains(createTime)) {
                String[] values = new String[tou.length];
                for (int i = 0; i < tou.length; i++) {
                    values[i] = csvReader.get(tou[i]);
                }
                csvWriter.writeRecord(values);
            }

        }
        csvWriter.close();
        csvReader.close();
    }

    @Test
    public void test3() {
        // ["东西","南北","前后"]
        String str = "[\"东西\",\"南北\",\"前后\"]";
        String[] split = parseStrArray(str);
        // 结果检测
        System.out.println(Arrays.asList(split));
    }

    /**
     * 该方法将字符串形式的数组转化为字符串数组
     *
     * @param str 字符串
     * @return 返回转化完的数组
     */
    public String[] parseStrArray(String str) {
        String substr = str.substring(2, str.length() - 2);
        String[] split = substr.split("\",\"");
        return split;
    }
}
