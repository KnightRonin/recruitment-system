package com.yisen.hbaseboot.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.dao.AllCountryJobDao;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.ResultToString;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @AuthorList: LiuYiSen, LuWei
 * @Date: 2020/6/4 15:46
 */
@Repository
public class AllCountryJobDaoImpl implements AllCountryJobDao {
    @Autowired
    Connection connection;
    @Autowired
    TableName tableName;

    /**
     * 返回近30日每天职位数量
     *
     * @param date "2020-6-4" String 类型
     * @return
     * @throws DaoException 数据库表连接或信息获取异常
     * @throws IOException  数据库表连接关闭异常
     */
    @Override
    public Map<String, Integer> getCountryEveryDay(String date, String family, String col) throws DaoException {
        Table table = null;
        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        String[] time = date.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(time[0]),
                Integer.parseInt(time[1]),
                Integer.parseInt(time[2]));

        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            String regex = date;
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            for (int i = 0; i < 31; i++) {
                String key = dateTimeFormatter.format(localDate);
                map.put(key, 0);
                localDate = localDate.plusDays(-1);
                SingleColumnValueFilter filter = new SingleColumnValueFilter(
                        Bytes.toBytes(family),
                        Bytes.toBytes(col),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(key)
                );
                filterList.addFilter(filter);
            }
            scan.setFilter(filterList);
            scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(col));
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                Cell cell = result.rawCells()[0];
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String key = value.substring(date.indexOf("/") + 1);
//                System.out.println(key);
                map.put(key, map.get(key) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("获取信息失败");
        } finally {
            ResultService.close(table);
        }
        return map;
    }

    /**
     * 返回与学历相关的职位招聘信息
     *
     * @param family
     * @param education
     * @param thirdType
     * @return
     * @throws DaoException
     */
    @Override
    public List<Map<String, Integer>> getEducationPosition(String family, String education, String thirdType) throws DaoException {
        Table table = null;
        Map<String, Integer> jcMap = new LinkedHashMap<String, Integer>();
        Map<String, Integer> rccMap = new LinkedHashMap<String, Integer>();
        String edu_jc = "大专及以下,大专,不限,中专,技师";
        String edu_rcc = "本科及以上,本科,博士,硕士,研究生";
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(education));
            scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(thirdType));
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                String edu = Bytes.toString(CellUtil.cloneValue(cells[0]));
                String third = Bytes.toString(CellUtil.cloneValue(cells[1]));

                if (edu_jc.contains(edu)) {
                    if (jcMap.containsKey(third)) {
                        jcMap.put(third, jcMap.get(third) + 1);
                    } else {
                        jcMap.put(third, 1);
                    }
                } else if (edu_rcc.contains(edu)) {
                    if (rccMap.containsKey(third)) {
                        rccMap.put(third, rccMap.get(third) + 1);
                    } else {
                        rccMap.put(third, 1);
                    }
                } else {
                    System.out.println("edu:" + edu);
                }
            }
//            ResultToString.resultScannerShow(scanner);

        } catch (Exception e) {
            throw new DaoException("获取信息失败");
        } finally {
            ResultService.close(table);
        }

        //结果封装
        ArrayList<Map<String, Integer>> list = new ArrayList<>();
        // 将map 集合 以值(Integer类型)  来排序
        jcMap = DataService.getValueSortMap(jcMap, 13);
        rccMap = DataService.getValueSortMap(rccMap, 13);
        list.add(jcMap);
        list.add(rccMap);

        return list;
    }

    /**
     * 返回排行相关信息。
     *
     * @param family
     * @param col    必须col[0] = date对应的列名 col[1] = secondType对应的列名 col[2] = skill对应的列名，
     *               col[3] = region对应的列名 col[4] = region的下一级区域对应的列名
     * @return list中每一个map都是一行数据，map<列名,列值>
     */
    @Override
    public List<Map<String, String>> getTopDetails(String family, String date,
                                                   String[] secondType, String region,
                                                   String... col) throws DaoException {
        System.out.println(date);
        Table table = null;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            //添加返回列
            for (String s : col) {
                scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(s));
            }
            //过滤时间范围
            SingleColumnValueFilter columnValueFilter = new SingleColumnValueFilter(Bytes.toBytes(family),
                    Bytes.toBytes(col[0]),
                    CompareOperator.EQUAL,
                    new SubstringComparator(date));
            //对应职位过滤
            FilterList filterList1 = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            for (String s : secondType) {
                SingleColumnValueFilter columnValueFilter1 = new SingleColumnValueFilter(Bytes.toBytes(family),
                        Bytes.toBytes(col[1]),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(s));
                filterList1.addFilter(columnValueFilter1);
            }
            //对应地区做相应处理
            if (region != null) {
                SingleColumnValueExcludeFilter columnValueFilter2 = new SingleColumnValueExcludeFilter(Bytes.toBytes(family),
                        Bytes.toBytes(col[3]),
                        CompareOperator.EQUAL,
                        new SubstringComparator(region));
                filterList.addFilter(columnValueFilter2);
//                scan.setFilter(columnValueFilter2);
            }

            filterList.addFilter(columnValueFilter);
            filterList.addFilter(filterList1);
            scan.setFilter(filterList);

            //获取信息
            ResultScanner scanner = table.getScanner(scan);
//            ResultToString.resultScannerShow(scanner);
            for (Result result : scanner) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                for (Cell cell : result.rawCells()) {
                    map.put(Bytes.toString(CellUtil.cloneQualifier(cell)),
                            Bytes.toString(CellUtil.cloneValue(cell)));
                }
                list.add(map);
            }
        } catch (Exception e) {
            throw new DaoException("获取信息失败");
        } finally {
            ResultService.close(table);
        }
        return list;
    }

    /**
     * 获取所有公司规模数据，并统计公司规模不同的公司分组数量情况
     *
     * @param family
     * @param col
     * @return
     */
    @Override
    public Map<String, Integer> getCompanyScale(String family, String col) throws DaoException {
        Table table = null;
        Map<String, Integer> map = new LinkedHashMap<>();
        // key 封装
        String[] keys = {"少于15人", "15-50人", "50-150人", "150-500人", "500-2000人", "2000人以上"};
        for (String key : keys) {
            map.put(key, 0);
        }
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(col));
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                String key = Bytes.toString(CellUtil.cloneValue(cells[0]));
                if (map.containsKey(key)) {
                    map.put(key, map.get(key) + 1);
                }
            }
        } catch (Exception e) {
            throw new DaoException("获取信息失败" + e.getStackTrace());
        } finally {
            ResultService.close(table);
        }

        return map;
    }

    /**
     * 全国公司福利情况统计
     *
     * @param family
     * @param col
     * @return
     */
    @Override
    public Map<String, Integer> getCompanyWelfare(String family, String col) throws DaoException {
        Table table = null;
        Map<String, Integer> map = new LinkedHashMap<>();
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(col));
            ResultScanner scanner = table.getScanner(scan);

            for (Result result : scanner) {
                String string = Bytes.toString(result.getValue(Bytes.toBytes(family), Bytes.toBytes(col)));
                if (!string.equals("[]") & !string.equals("")) {
                    List<String> strings = JSONArray.parseArray(string, String.class);
                    for (String s : strings) {
                        if (map.containsKey(s)) {
                            map.put(s, map.get(s) + 1);
                        } else {
                            map.put(s, 1);
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
        map = DataService.getValueSortMap(map, 0);
        return map;
    }
}
