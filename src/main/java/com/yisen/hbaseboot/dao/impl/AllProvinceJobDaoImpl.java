package com.yisen.hbaseboot.dao.impl;

import com.yisen.hbaseboot.dao.AllProvinceJobDao;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class AllProvinceJobDaoImpl implements AllProvinceJobDao {
    @Autowired
    Connection connection;
    @Autowired
    TableName tableName;

    /**
     * @author: LiuYiSen
     * @description: 添加一个用户
     * @throws:DaoException: 查询信息出错 | IOException: 关闭数据库连接出错
     */
    @Override
    public Map<String, Integer> getAllProvinceJob(String family, String col) throws DaoException {
        Table table = null;
        ResultScanner scanner = null;
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        try {
            table = connection.getTable(tableName);
            ;
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(col));
            scanner = table.getScanner(scan);
            //中国地图
            String[] china = {"上海", "云南", "北京", "四川", "天津", "安徽", "山东", "山西", "广东", "广西",
                    "江苏", "江西", "河北", "河南", "浙江", "海南", "湖北", "湖南", "福建", "辽宁",
                    "重庆", "陕西", "贵州", "青海", "甘肃", "西藏", "黑龙江", "宁夏", "内蒙古", "吉林",
                    "新疆", "台湾", "澳门", "香港", "南海诸岛"};
            for (String s : china) {
                map.put(s, 0);
            }
            // 数据清洗
            for (Result result : scanner) {
                for (Cell cell : result.rawCells()) {
                    Set<String> keySet = map.keySet();
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    for (String s : keySet) {
                        if (s.equals(value)) {
                            map.put(s, map.get(s) + 1);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("获取信息失败");
        } finally {
            ResultService.close(table);
        }
        //打印dao结果
//        ResultToString.resultScannerShow(scanner);
        return map;
    }
}
