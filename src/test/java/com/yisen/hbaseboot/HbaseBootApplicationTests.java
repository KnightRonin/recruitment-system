package com.yisen.hbaseboot;

import com.yisen.hbaseboot.dao.IfDao;
import com.yisen.hbaseboot.dao.PostDao;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.client.*;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest
class HbaseBootApplicationTests {
    @Autowired
    Connection connection;
    @Autowired
    PostDao postDao;
    @Autowired
    IfDao ifDao;

    @Test
    void contextLoads() throws IOException, DaoException {
//        InputStreamReader input = new InputStreamReader(new FileInputStream("D:\\eclipse\\hbase_boot\\src\\test\\java\\com\\yisen\\hbaseboot\\new_posdetail.csv"));
//        BufferedReader br = new BufferedReader(input);
//        String line = br.readLine();
//        while (true){
//            line = br.readLine();
//            if (line!=null){
//                Map<String, String> map = new LinkedHashMap<>();
//                line = line.replace("\"\"","\'");
//                line = line.substring(1, line.length()-1);
//                String[] split = line.split("\",\"");
//                String[] lie = {"positionId","positionName","companyLogo","positionLabels","positionAdvantage","positionDesc","salary","city","latitude","longtitude","district","workYear","education","jobNature","companyName","companyLabelList","companySize","companyId","firstType","secondType","thirdType","financeStage","city_province","createTime"};
//                for (int i = 0; i < split.length; i++) {
//                    map.put(lie[i],split[i]);
//                }
//                map.put("row", map.get("positionId"));
//                postDao.postSingleRecord(map);
//            }else{
//                break;
//            }
//        }
        ifDao.ifPositionId("7107575");
    }


}
