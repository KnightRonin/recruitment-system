package com.yisen.hbaseboot.dao.impl;

import com.yisen.hbaseboot.dao.LabDao;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.ResultToString;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

/**
 * @AuthorList: LiuYiSen, LuWei
 * @Date: 2020/6/12 15:17
 */
@Repository
public class LabDaoImpl implements LabDao {
    @Autowired
    Connection connection;
    @Autowired
    TableName tableName;

    /**
     * 读取表格所需数据
     *
     * @param family
     * @param colMap key 是 列名， value 是要过滤的值。null代表全返回
     */
    @Override
    public List<Map<String, String>> getTableEnd(String family, Map<String, List<String>> colMap) throws DaoException {
        Table table = null;
        byte[] family_bt = Bytes.toBytes(family);
//        System.out.println(colMap);
        List<Map<String, String>> list = null;
        try {
            // scan 直接添加的 总过滤器
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            for (String s : colMap.keySet()) {
                scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(s));
            }
            // 时间列的过滤器
            FilterList dateFilter = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            for (String createTime : colMap.get("createTime")) {
                SingleColumnValueFilter singleFilter = new SingleColumnValueFilter(family_bt,
                        Bytes.toBytes("createTime"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(createTime));
                dateFilter.addFilter(singleFilter);
            }
            // 地区顾虑器
            FilterList positionFilter = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            if (colMap.get("city_province") != null) {
                for (String cityProvince : colMap.get("city_province")) {
                    SingleColumnValueFilter provinceFilter = new SingleColumnValueFilter(family_bt,
                            Bytes.toBytes("city_province"),
                            CompareOperator.EQUAL,
                            new SubstringComparator(cityProvince));
                    positionFilter.addFilter(provinceFilter);
                }
            }
            if (colMap.get("city") != null) {
                for (String city : colMap.get("city")) {
                    SingleColumnValueFilter cityFilter = new SingleColumnValueFilter(family_bt,
                            Bytes.toBytes("city"),
                            CompareOperator.EQUAL,
                            new SubstringComparator(city));
                    positionFilter.addFilter(cityFilter);
                }
            }

            // 给scan添加过滤器
            filterList.addFilter(dateFilter);
            filterList.addFilter(positionFilter);
            scan.setFilter(filterList);
            // 拿到所有数据结果
            ResultScanner scanner = table.getScanner(scan);
//            ResultToString.resultScannerShow(scanner);
            // 结果简单封装
            list = new ArrayList<Map<String, String>>();
            Set<String> keys = colMap.keySet();
            for (Result result : scanner) {
                Map<String, String> map = new LinkedHashMap<>();
                for (String key : keys) {
                    String value = Bytes.toString(result.getValue(family_bt, Bytes.toBytes(key)));
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("获取信息失败");
        } finally {
            ResultService.close(table);
        }
        return list;
    }

    /**
     * 数据可视化
     *
     * @param family  列族
     * @param colList 所有列名
     * @param times   所有事件刻度
     * @return list是所有数据，map是一行数据
     */
    @Override
    public List<Map<String, String>> getTimesData(String family, List<String> colList, List<String> times) throws DaoException {
        Table table = null;
        byte[] byFamily = Bytes.toBytes(family);
        List<Map<String, String>> list = new ArrayList<>();
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            for (String s : colList) {
                scan.addColumn(byFamily, Bytes.toBytes(s));
            }
            // 总
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            // 时间过滤器
            FilterList filterList1 = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            for (String time : times) {
                filterList1.addFilter(new SingleColumnValueFilter(byFamily,
                        Bytes.toBytes("createTime"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(time)));
            }
            filterList.addFilter(filterList1);
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                Map<String, String> map = new LinkedHashMap<>();
                for (Cell cell : result.rawCells()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            throw new DaoException("数据获取失败");
        } finally {
            ResultService.close(table);
        }
        return list;
    }

    /**
     * 返回指定日期，指定的职位信息
     *
     * @param times
     * @param positions
     * @return
     */
    @Override
    public List<Map<String, String>> getTimePosition(List<String> times, List<String> positions) {
        Table table = null;
        byte[] family = Bytes.toBytes("testfamily");
        List<Map<String, String>> list = null;
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(family, Bytes.toBytes("createTime"));
            scan.addColumn(family, Bytes.toBytes("secondType"));
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL); // 总过滤器
            FilterList timeFilter = new FilterList(FilterList.Operator.MUST_PASS_ONE); // 时间列过滤器
            for (String time : times) {
                SingleColumnValueFilter filter = new SingleColumnValueFilter(family,
                        Bytes.toBytes("createTime"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(time));
                timeFilter.addFilter(filter);
            }
            FilterList positionFilter = new FilterList(FilterList.Operator.MUST_PASS_ONE); // 职位列过滤器
            for (String position : positions) {
                SingleColumnValueFilter filter = new SingleColumnValueFilter(family,
                        Bytes.toBytes("secondType"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(position));
                positionFilter.addFilter(filter);
            }
            filterList.addFilter(timeFilter);
            filterList.addFilter(positionFilter);
            scan.setFilter(filterList);// 给scan 设置filter
            ResultScanner scanner = table.getScanner(scan);
//            ResultToString.resultScannerShow(scanner);
            list = ResultToString.getMap(scanner);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ResultService.close(table);
        }

        return list;
    }
}
