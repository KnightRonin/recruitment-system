package com.yisen.wechat.dao;

import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.ResultToString;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.protobuf.generated.FilterProtos;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/8/4 10:35
 */
@Repository
public class WeChatDao {
    @Autowired
    Connection connection;
    @Autowired
    TableName tableName;
    @Autowired
    String family;

    public List<Map<String, String>> getCompare(String date) throws DaoException {
        List<Map<String, String>> map = null;
        byte[] familys = Bytes.toBytes(family);
        byte[] secondType = Bytes.toBytes("secondType");
        byte[] companyFullName = Bytes.toBytes("companyFullName");
        byte[] createTime = Bytes.toBytes("createTime");
        byte[] city = Bytes.toBytes("city");
        byte[] jobNature = Bytes.toBytes("jobNature");
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            scan.addColumn(familys, secondType);
            scan.addColumn(familys, companyFullName);
            scan.addColumn(familys, createTime);
            scan.addColumn(familys, city);
            scan.addColumn(familys, jobNature);
            SingleColumnValueExcludeFilter filter = new SingleColumnValueExcludeFilter(familys,
                    createTime,
                    CompareOperator.EQUAL,
                    Bytes.toBytes(date));
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            map = ResultToString.getMap(scanner);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("获取数据失败", 500);
        }
        return map;
    }

    public List<Map<String, String>> posTrend(List<String> date, String city) throws DaoException {
        List<Map<String, String>> list = new ArrayList<>();
        byte[] familys = Bytes.toBytes(family);
        byte[] createTime = Bytes.toBytes("createTime");
        byte[] thirdType = Bytes.toBytes("thirdType");
        byte[] cities = Bytes.toBytes("city");
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            scan.addColumn(familys, createTime);
            scan.addColumn(familys, thirdType);
            scan.addColumn(familys, cities);
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            FilterList filterList1 = new FilterList(FilterList.Operator.MUST_PASS_ONE);// 时间过滤器
            for (String time : date) {
                SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(familys,
                        createTime,
                        CompareOperator.EQUAL,
                        Bytes.toBytes(time));
                filterList1.addFilter(singleColumnValueFilter);
            }
            if (city != null && !city.equals("")) {
                SingleColumnValueExcludeFilter cityFilter = new SingleColumnValueExcludeFilter(familys,
                        cities,
                        CompareOperator.EQUAL,
                        Bytes.toBytes(city)); // 城市过滤器
                filterList.addFilter(cityFilter);
            }

            filterList.addFilter(filterList1);
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            list = ResultToString.getMap(scanner);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("获取数据失败", 500);
        }

        return list;
    }

    /**
     * 全国所有薪资
     *
     * @return
     * @throws DaoException
     */
    public List<Map<String, String>> countrySalary() throws DaoException {
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes("testfamily"), Bytes.toBytes("salary"));
            ResultScanner scanner = table.getScanner(scan);
            List<Map<String, String>> list = ResultToString.getMap(scanner);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("获取数据失败", 500);
        }
    }


}
