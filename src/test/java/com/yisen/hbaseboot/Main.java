package com.yisen.hbaseboot;

import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.ResultToString;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @AuthorList: HuangGuoFu
 * @Date: 2022/3/6 9:55
 */
public class Main {
    public static void main(String[] args) throws IOException, DaoException {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "175.178.196.83");
        conf.set("hbase.client.keyvalue.maxsize", "500000");
        Connection connection = ConnectionFactory.createConnection(conf);

//        InputStreamReader input = new InputStreamReader(new FileInputStream("./pos_detail.csv"));
//        BufferedReader br = new BufferedReader(input);
//        String line = br.readLine();
////        while (true){
//            line = br.readLine();
//            if (line!=null){
//                Map<String, String> map = new LinkedHashMap<>();
////                line = line.replace("\"\"","\'");
//                line = line.substring(1, line.length()-1);
//                String[] split = line.split("\",\"");
//                String[] lie = {"positionId","positionName","companyLogo","positionLabels","positionAdvantage","positionDesc","salary","city","latitude","longtitude","district","workYear","education","jobNature","companyName","companyLabelList","companySize","companyId","firstType","secondType","thirdType","financeStage","city_province","createTime"};
//                for (int i = 0; i < split.length; i++) {
//                    map.put(lie[i],split[i]);
//                }
//                map.put("row", map.get("positionId"));
        Table table = null;
        byte[] family = Bytes.toBytes("testfamily");
        try {
            table = connection.getTable(TableName.valueOf("boot"));
            Scan scan = new Scan();
            scan.addColumn(family, Bytes.toBytes("aaa"));
            ResultScanner scanner = table.getScanner(scan);
            List<Map<String, String>> map = ResultToString.getMap(scanner);
            System.out.println(map);
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            table.close();
            connection.close();
        }
    }
}
