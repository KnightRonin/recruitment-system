package com.yisen.hbaseboot.dao.impl;

import com.yisen.hbaseboot.dao.JobAnalyzeDao;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.exception.DaoException;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/27 10:08
 */
@Repository
public class JobAnalyzeDaoImpl implements JobAnalyzeDao {
    @Autowired
    Connection connection;
    @Autowired
    TableName tableName;
    @Autowired
    String family;

    @Override
    public List<Map<String, String>> getExpScal(String region, String level, String position) throws DaoException {
        Table table = null;
        List<Map<String, String>> list = new ArrayList<>();
        byte[] familys = Bytes.toBytes(family);
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(familys, Bytes.toBytes("workYear"));
            scan.addColumn(familys, Bytes.toBytes("salary"));
            if (!position.equals("")) {
                scan.addColumn(familys, Bytes.toBytes("secondType"));
                SingleColumnValueExcludeFilter positionFilter = new SingleColumnValueExcludeFilter(familys,
                        Bytes.toBytes("secondType"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(position));
                filterList.addFilter(positionFilter);
            }
            if (!region.equals("")) {
                if (level.equals("1")) {

                } else if (level.equals("2")) {
                    scan.addColumn(familys, Bytes.toBytes("city_province"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city_province"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                } else if (level.equals("3")) {
                    scan.addColumn(familys, Bytes.toBytes("city"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                }
            }
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                for (Cell cell : result.rawCells()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            throw new DaoException("信息获取失败");
        } finally {
            ResultService.close(table);
        }
//        System.out.println(list.size());
        return list;
    }

    @Override
    public List<Map<String, String>> getCompanySize(String region, String level, String position) throws DaoException {
        Table table = null;
        List<Map<String, String>> list = new ArrayList<>();
        byte[] familys = Bytes.toBytes(family);
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(familys, Bytes.toBytes("companySize"));
            if (!position.equals("")) {
                scan.addColumn(familys, Bytes.toBytes("secondType"));
                SingleColumnValueExcludeFilter positionFilter = new SingleColumnValueExcludeFilter(familys,
                        Bytes.toBytes("secondType"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(position));
                filterList.addFilter(positionFilter);
            }
            if (!region.equals("")) {
                if (level.equals("1")) {

                } else if (level.equals("2")) {
                    scan.addColumn(familys, Bytes.toBytes("city_province"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city_province"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                } else if (level.equals("3")) {
                    scan.addColumn(familys, Bytes.toBytes("city"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                }
            }
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                for (Cell cell : result.rawCells()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            throw new DaoException("信息获取失败");
        } finally {
            ResultService.close(table);
        }
//        System.out.println(list.size());
        return list;
    }

    @Override
    public List<Map<String, String>> getEduInfo(String region, String level, String position) throws DaoException {
        Table table = null;
        List<Map<String, String>> list = new ArrayList<>();
        byte[] familys = Bytes.toBytes(family);
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(familys, Bytes.toBytes("education"));
            if (!position.equals("")) {
                scan.addColumn(familys, Bytes.toBytes("secondType"));
                SingleColumnValueExcludeFilter positionFilter = new SingleColumnValueExcludeFilter(familys,
                        Bytes.toBytes("secondType"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(position));
                filterList.addFilter(positionFilter);
            }
            if (!region.equals("")) {
                if (level.equals("1")) {

                } else if (level.equals("2")) {
                    scan.addColumn(familys, Bytes.toBytes("city_province"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city_province"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                } else if (level.equals("3")) {
                    scan.addColumn(familys, Bytes.toBytes("city"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                }
            }
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                for (Cell cell : result.rawCells()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            throw new DaoException("信息获取失败");
        } finally {
            ResultService.close(table);
        }
//        System.out.println(list.size());
        return list;
    }

    @Override
    public List<Map<String, String>> getCompanyWelfare(String region, String level, String position) throws DaoException {
        Table table = null;
        List<Map<String, String>> list = new ArrayList<>();
        byte[] familys = Bytes.toBytes(family);
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(familys, Bytes.toBytes("companyLabelList"));
            if (!position.equals("")) {
                scan.addColumn(familys, Bytes.toBytes("secondType"));
                SingleColumnValueExcludeFilter positionFilter = new SingleColumnValueExcludeFilter(familys,
                        Bytes.toBytes("secondType"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(position));
                filterList.addFilter(positionFilter);
            }
            if (!region.equals("")) {
                if (level.equals("1")) {

                } else if (level.equals("2")) {
                    scan.addColumn(familys, Bytes.toBytes("city_province"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city_province"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                } else if (level.equals("3")) {
                    scan.addColumn(familys, Bytes.toBytes("city"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                }
            }
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                for (Cell cell : result.rawCells()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            throw new DaoException("信息获取失败");
        } finally {
            ResultService.close(table);
        }
//        System.out.println(list.size());
        return list;
    }

    @Override
    public List<Map<String, String>> getCompanyFinance(String region, String level, String position) throws DaoException {
        Table table = null;
        List<Map<String, String>> list = new ArrayList<>();
        byte[] familys = Bytes.toBytes(family);
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(familys, Bytes.toBytes("financeStage"));
            if (!position.equals("")) {
                scan.addColumn(familys, Bytes.toBytes("secondType"));
                SingleColumnValueExcludeFilter positionFilter = new SingleColumnValueExcludeFilter(familys,
                        Bytes.toBytes("secondType"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(position));
                filterList.addFilter(positionFilter);
            }
            if (!region.equals("")) {
                if (level.equals("1")) {

                } else if (level.equals("2")) {
                    scan.addColumn(familys, Bytes.toBytes("city_province"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city_province"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                } else if (level.equals("3")) {
                    scan.addColumn(familys, Bytes.toBytes("city"));
                    SingleColumnValueExcludeFilter locationFilter = new SingleColumnValueExcludeFilter(familys,
                            Bytes.toBytes("city"),
                            CompareOperator.EQUAL,
                            Bytes.toBytes(region.substring(0, 2)));
                    filterList.addFilter(locationFilter);
                }
            }
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                for (Cell cell : result.rawCells()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("信息获取失败");
        } finally {
            ResultService.close(table);
        }
//        System.out.println(list.size());
        return list;
    }

    @Override
    public List<Map<String, String>> getCountryExpScalEdu() throws DaoException {
        Table table = null;
        List<Map<String, String>> list = new ArrayList<>();
        byte[] familys = Bytes.toBytes(family);
        try {
            table = connection.getTable(tableName);
            Scan scan = new Scan();
            scan.addColumn(familys, Bytes.toBytes("education"));
            scan.addColumn(familys, Bytes.toBytes("salary"));
            scan.addColumn(familys, Bytes.toBytes("workYear"));
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
            e.printStackTrace();
            throw new DaoException("信息获取失败");
        } finally {
            ResultService.close(table);
        }

        return list;
    }
}
