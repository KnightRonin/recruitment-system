package com.yisen.hbaseboot.util;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultToString {
    public static void resultScannerShow(ResultScanner scanner) {
        for (Result result : scanner) {
            resultShow(result);
        }
    }

    public static void resultShow(Result result) {
        for (Cell cell : result.rawCells()) {
            System.out.println("rowkey: " + Bytes.toString(CellUtil.cloneRow(cell)) +
                    ", Qualifier: " + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                    ", Value: " + Bytes.toString(CellUtil.cloneValue(cell)));
        }
    }

    public static List<Map<String, String>> getMap(ResultScanner rs) {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        for (Result r : rs) {
            Map<String, String> map = new LinkedHashMap<>();
            for (Cell cell : r.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                if (value == null || value.equals("")) {
                    map = null;
                    break;
                }
                map.put(Bytes.toString(CellUtil.cloneQualifier(cell)),
                        value);
            }
            if (map == null || map.size() == 0)
                continue;
            list.add(map);
        }
        return list;
    }
}
