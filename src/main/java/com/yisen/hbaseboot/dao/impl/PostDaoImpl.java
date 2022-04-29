package com.yisen.hbaseboot.dao.impl;

import com.yisen.hbaseboot.dao.PostDao;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/5 14:09
 */
@Repository
public class PostDaoImpl implements PostDao {
    @Autowired
    Connection connection;

    @Override
    public void postSingleRecord(Map<String, String> map) throws DaoException {
//        System.out.println(map);
        Table table = null;
        byte[] family = Bytes.toBytes("testfamily");
        String[] lie = {"positionName", "positionId", "companyId", "salary", "city", "city_province", "district", "workYear", "education", "jobNature", "positionLables", "companyLabelList", "skillLables", "companyFullName", "companyShortName", "famousCompany", "industryField", "financeStage", "companySize", "companyLogo", "longitude", "latitude", "firstType", "secondType", "thirdType", "createTime"};
        try {
            table = connection.getTable(TableName.valueOf("boot"));
            Put put = new Put(Bytes.toBytes(map.get("row")));
            for (String s : lie) {
                put.addColumn(family, Bytes.toBytes(s), Bytes.toBytes(map.get(s)));
            }
            table.put(put);

        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("存储失败", 400);
        } finally {
            ResultService.close(table);
        }
    }
}
