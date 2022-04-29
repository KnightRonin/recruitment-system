package com.yisen.hbaseboot.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.dao.PositionInfoDao;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.util.ResultService;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hbase.thirdparty.org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/29 18:56
 */
@Repository
public class PositionInfoDaoImpl implements PositionInfoDao {
    @Autowired
    Connection connection;
    @Autowired
    TableName tableName;

    /**
     * 获取单条职位信息详情
     *
     * @param pid 对应positionID
     * @return
     */
    @Override
    public Map<String, String> getPosDetail(String pid) {
        Table table = null;
        Map<String, String> map = new LinkedHashMap<>();
        try {
            table = connection.getTable(tableName);
            Get get = new Get(Bytes.toBytes(pid));
            Result result = table.get(get);
            for (Cell cell : result.rawCells()) {
                String key = Bytes.toString(CellUtil.cloneQualifier(cell));
//                System.out.println(key);
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                map.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ResultService.close(table);
        }
        return map;
    }

    @Override
    public Map<String, Collection> getClassification(String city) {
        Map<String, Collection> linkMap = new LinkedHashMap<>();
        try (Table table = connection.getTable(tableName);) {
            // 数据库列值
            byte[] family = Bytes.toBytes("testfamily");
            byte[] salary = Bytes.toBytes("salary");
            byte[] cities = Bytes.toBytes("city");
            // 添加查询列
            Scan scan = new Scan();
            scan.addColumn(family, salary);
            scan.addColumn(family, cities);
            // 过滤器
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            SingleColumnValueExcludeFilter cityFilter = new SingleColumnValueExcludeFilter(family,
                    cities,
                    CompareOperator.EQUAL,
                    Bytes.toBytes(city));
            filterList.addFilter(cityFilter);
            scan.setFilter(filterList);
            // 查询
//            System.out.println(city);
            ResultScanner scanner = table.getScanner(scan);
            // 分类统计
            ArrayList<Integer> salaryList = new ArrayList<>();
            for (Result result : scanner) {
                String salary_s = Bytes.toString(result.getValue(family, salary));
//                System.out.println(salary_s);
                if (salary_s.contains("k")) {
                    salary_s = salary_s.substring(salary_s.indexOf("-") + 1, salary_s.indexOf("k", salary_s.indexOf("-") + 1));
                } else {
                    salary_s = salary_s.substring(salary_s.indexOf("-") + 1, salary_s.indexOf("K", salary_s.indexOf("-") + 1));
                }
                salaryList.add(Integer.parseInt(salary_s));
            }
            linkMap.put("salary", salaryList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkMap;
    }

    @Override
    public List<Map<String, String>> getPosList() {
        List<Map<String, String>> list = new ArrayList<>();
        try (Table table = connection.getTable(tableName)) {
            // 所有列
            byte[] family = Bytes.toBytes("testfamily");
            byte[] positionId = Bytes.toBytes("positionId");
            byte[] positionName = Bytes.toBytes("positionName");
            byte[] companyShortName = Bytes.toBytes("companyShortName");
            byte[] companyLogo = Bytes.toBytes("companyLogo");
            byte[] city = Bytes.toBytes("city");
            byte[] workYear = Bytes.toBytes("workYear");
            byte[] education = Bytes.toBytes("education");
            byte[] famousCompany = Bytes.toBytes("famousCompany");
            byte[] salary = Bytes.toBytes("salary");
            Scan scan = new Scan();
            // 添加查询列
            scan.addColumn(family, positionId);
            scan.addColumn(family, positionName);
            scan.addColumn(family, companyShortName);
            scan.addColumn(family, companyLogo);
            scan.addColumn(family, city);
            scan.addColumn(family, workYear);
            scan.addColumn(family, education);
            scan.addColumn(family, famousCompany);
            scan.addColumn(family, salary);
            // 分页数据过滤器
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
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
//            System.out.println(list.size()+"----"+size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据条件获取分页数据
     *
     * @param key
     * @param location
     * @param jsonObject
     * @return
     */
    @Override
    public List<Map<String, String>> searchPos(String key, String location, JSONObject jsonObject) {
        List<Map<String, String>> list = null;
        String[] cols = {"testfamily", "positionId", "positionName", "companyShortName",
                "companyLogo", "city", "workYear", "education", "salary", "famousCompany",
                "secondType", "thirdType"};
        List<String> colArray = Arrays.asList(cols);
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            // 列族和字段
            byte[] family = Bytes.toBytes("testfamily");
            byte[] positionId = Bytes.toBytes("positionId");
            byte[] positionName = Bytes.toBytes("positionName");
            byte[] companyShortName = Bytes.toBytes("companyShortName");
            byte[] companyLogo = Bytes.toBytes("companyLogo");
            byte[] city = Bytes.toBytes("city");
            byte[] workYear = Bytes.toBytes("workYear");
            byte[] education = Bytes.toBytes("education");
            byte[] salary = Bytes.toBytes("salary");
            byte[] famousCompany = Bytes.toBytes("famousCompany");
            //  只过滤不返回的列
            byte[] secondType = Bytes.toBytes("secondType");
            byte[] thirdType = Bytes.toBytes("thirdType");
            //添加返回列
            scan.addColumn(family, positionId);
            scan.addColumn(family, positionName);
            scan.addColumn(family, companyShortName);
            scan.addColumn(family, companyLogo);
            scan.addColumn(family, city);
            scan.addColumn(family, workYear);
            scan.addColumn(family, education);
            scan.addColumn(family, salary);
            scan.addColumn(family, famousCompany);
            scan.addColumn(family, secondType);
            scan.addColumn(family, thirdType);

            // 总过滤
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);

            // 字段包含值过滤
            FilterList textFilterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            SingleColumnValueFilter positionNameFilter = new SingleColumnValueFilter(family,
                    positionName,
                    CompareOperator.EQUAL,
                    new SubstringComparator(key));
            SingleColumnValueFilter companyShortNameFilter = new SingleColumnValueFilter(family,
                    companyShortName,
                    CompareOperator.EQUAL,
                    new SubstringComparator(key));
            SingleColumnValueExcludeFilter secondTypeFilter = new SingleColumnValueExcludeFilter(family,
                    secondType,
                    CompareOperator.EQUAL,
                    new SubstringComparator(key));
            SingleColumnValueExcludeFilter thirdTypeFilter = new SingleColumnValueExcludeFilter(family,
                    thirdType,
                    CompareOperator.EQUAL,
                    new SubstringComparator(key));
            textFilterList.addFilter(positionNameFilter);
            textFilterList.addFilter(companyShortNameFilter);
            textFilterList.addFilter(secondTypeFilter);
            textFilterList.addFilter(thirdTypeFilter);
            // location 过滤器
            if (location != null && !location.equals("")) {
                SingleColumnValueFilter cityFilter = new SingleColumnValueFilter(family,
                        city,
                        CompareOperator.EQUAL,
                        Bytes.toBytes(location));
                filterList.addFilter(cityFilter);
            }
            // JSONObject 过滤条件
            if (jsonObject != null && jsonObject.getInnerMap().keySet().size() != 0) {
                for (String k : jsonObject.getInnerMap().keySet()) {
                    JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString(k));
                    List<String> values = jsonArray.toJavaList(String.class);
                    FilterList allList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
                    for (String value : values) {
                        if (!colArray.contains(k)) {
                            scan.addColumn(family, Bytes.toBytes(k));
                            SingleColumnValueExcludeFilter singleColumnValueFilter = new SingleColumnValueExcludeFilter(family,
                                    Bytes.toBytes(k),
                                    CompareOperator.EQUAL,
                                    Bytes.toBytes(value));
                            allList.addFilter(singleColumnValueFilter);
                        } else {
                            SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(family,
                                    Bytes.toBytes(k),
                                    CompareOperator.EQUAL,
                                    Bytes.toBytes(value));
                            allList.addFilter(singleColumnValueFilter);
                        }
                    }
                    filterList.addFilter(allList);
                }
            }
            //添加过滤器
            filterList.addFilter(textFilterList);
            scan.setFilter(filterList);
            // 获取数据
            ResultScanner scanner = table.getScanner(scan);
            list = new ArrayList<>();
            for (Result result : scanner) {
                Map<String, String> map = new LinkedHashMap<>();
                for (Cell cell : result.rawCells()) {
                    String k = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    map.put(k, value);
                }
                list.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
