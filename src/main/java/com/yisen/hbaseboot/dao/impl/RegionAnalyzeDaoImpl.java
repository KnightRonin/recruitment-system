package com.yisen.hbaseboot.dao.impl;

import com.yisen.hbaseboot.dao.RegionAnalyzeDao;
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
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/9/10 14:22
 */
@Repository
public class RegionAnalyzeDaoImpl implements RegionAnalyzeDao {
    @Autowired
    Connection connection;
    @Autowired
    TableName tableName;
    @Autowired
    String family;

    @Override
    public List<Map<String, String>> provTrend(String region, List<String> date) throws DaoException {
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            byte[] familys = Bytes.toBytes(family);
            String[] cols = {"createTime", "city_province", "secondType"};// 添加查询列
            for (String col : cols) {
                scan.addColumn(familys, Bytes.toBytes(col));
            }
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);// 总过滤器
            FilterList dateFilterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);// 日期过滤器
            for (String s : date) {
                SingleColumnValueFilter dateFilter = new SingleColumnValueFilter(familys,
                        Bytes.toBytes("createTime"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(s));
                dateFilterList.addFilter(dateFilter);
            }
            SingleColumnValueExcludeFilter provinceFilter = new SingleColumnValueExcludeFilter(familys,
                    Bytes.toBytes("city_province"),
                    CompareOperator.EQUAL,
                    Bytes.toBytes(region));// 地区过滤器
            // 添加过滤器
            filterList.addFilter(provinceFilter);
            filterList.addFilter(dateFilterList);
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            List<Map<String, String>> map = ResultToString.getMap(scanner);
            return map;

        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("获取数据失败,请检查数据库hbase状态", 500);
        }
    }

    /**
     * 返回传入省份的工资，工作年份，学历要求，公司融资情况，公司大小，工作性质
     *
     * @param region
     * @return
     * @throws DaoException
     */
    @Override
    public List<Map<String, String>> getStandard(String region) throws DaoException {
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            byte[] familys = Bytes.toBytes(family);
            String[] cols = {"salary", "workYear", "education", "financeStage", "companySize", "jobNature", "city_province"};// 添加查询列
            for (String col : cols) {
                scan.addColumn(familys, Bytes.toBytes(col));
            }
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);// 总过滤器
            SingleColumnValueExcludeFilter provinceFilter = new SingleColumnValueExcludeFilter(familys,
                    Bytes.toBytes("city_province"),
                    CompareOperator.EQUAL,
                    Bytes.toBytes(region));// 地区过滤器
            filterList.addFilter(provinceFilter);
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            List<Map<String, String>> map = ResultToString.getMap(scanner);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("获取数据失败,请检查数据库hbase状态", 500);
        }
    }

    /**
     * 返回全国的工资，工作年份，学历要求，公司融资情况，公司大小，工作性质
     *
     * @return
     * @throws DaoException
     */
    @Override
    public List<Map<String, String>> getStandard() throws DaoException {
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            byte[] familys = Bytes.toBytes(family);
            String[] cols = {"salary", "workYear", "education", "financeStage", "companySize", "jobNature"};// 添加查询列
            for (String col : cols) {
                scan.addColumn(familys, Bytes.toBytes(col));
            }
            ResultScanner scanner = table.getScanner(scan);
            List<Map<String, String>> map = ResultToString.getMap(scanner);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("获取数据失败,请检查数据库hbase状态", 500);
        }
    }

    @Override
    public List<Map<String, String>> getScatter(String region, String day) throws DaoException {
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            byte[] familys = Bytes.toBytes(family);
            String[] cols = {"city_province", "salary", "secondType", "createTime"};// 添加查询列
            for (String col : cols) {
                scan.addColumn(familys, Bytes.toBytes(col));
            }
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);// 总过滤器
            SingleColumnValueExcludeFilter provinceFilter = new SingleColumnValueExcludeFilter(familys,
                    Bytes.toBytes("city_province"),
                    CompareOperator.EQUAL,
                    Bytes.toBytes(region));// 地区过滤器
            SingleColumnValueFilter createTimeFilter = new SingleColumnValueFilter(familys,
                    Bytes.toBytes("createTime"),
                    CompareOperator.EQUAL,
                    Bytes.toBytes(day));
            filterList.addFilter(provinceFilter);
            filterList.addFilter(createTimeFilter);
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            List<Map<String, String>> map = ResultToString.getMap(scanner);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("获取数据失败,请检查数据库hbase状态", 500);
        }
    }

    @Override
    public List<Map<String, String>> getScatter(String region, String day, Set<String> secondType) throws DaoException {
        try (Table table = connection.getTable(tableName)) {
            Scan scan = new Scan();
            byte[] familys = Bytes.toBytes(family);
            String[] cols = {"city_province", "salary", "secondType", "createTime"};// 添加查询列
            for (String col : cols) {
                scan.addColumn(familys, Bytes.toBytes(col));
            }
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);// 总过滤器
            SingleColumnValueExcludeFilter provinceFilter = new SingleColumnValueExcludeFilter(familys,
                    Bytes.toBytes("city_province"),
                    CompareOperator.EQUAL,
                    Bytes.toBytes(region));// 地区过滤器
            SingleColumnValueFilter createTimeFilter = new SingleColumnValueFilter(familys,
                    Bytes.toBytes("createTime"),
                    CompareOperator.EQUAL,
                    Bytes.toBytes(day));
            FilterList secondTypeFilters = new FilterList(FilterList.Operator.MUST_PASS_ONE);// 职位过滤器
            for (String str : secondType) {
                SingleColumnValueFilter secondTypeFilter = new SingleColumnValueFilter(familys,
                        Bytes.toBytes("secondType"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(str));
                secondTypeFilters.addFilter(secondTypeFilter);
            }
            filterList.addFilter(secondTypeFilters);
            filterList.addFilter(provinceFilter);
            filterList.addFilter(createTimeFilter);
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            List<Map<String, String>> map = ResultToString.getMap(scanner);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("获取数据失败,请检查数据库hbase状态", 500);
        }
    }
}
