package com.yisen.hbaseboot.dao.impl;

import com.yisen.hbaseboot.dao.IfDao;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.ResultToString;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/5 15:27
 */
@Service
public class IfDaoImpl implements IfDao {
    @Autowired
    Connection connection;

    @Override
    public int ifPositionId(String id) throws DaoException {
        Table table = null;
        ArrayList<String> que = new ArrayList<>();
        int size = 0;
        try {
            table = connection.getTable(TableName.valueOf("boot"));
            Get get = new Get(Bytes.toBytes(id));
            get.addColumn(Bytes.toBytes("family1"), Bytes.toBytes("positionId"));
            Result result = table.get(get);
            for (Cell cell : result.rawCells()) {
                String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                que.add(key + "----");
            }
            for (String s : que) {
                System.out.println(s);
            }
            size = result.size();
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("服务器开小差了", 500);
        } finally {
            ResultService.close(table);
        }
        return size;
    }
}
