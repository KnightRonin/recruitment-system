package com.yisen.hbaseboot.util;

import com.yisen.hbaseboot.entity.JobData;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/4 19:25
 */
public class ResultService {
    /**
     * 此方法将 map 转换为 list 用于返回结果为 JobData 的数据封装
     *
     * @param map
     * @return
     */
    public static List<JobData> parseList(Map<String, Integer> map) {
        List<JobData> list = new ArrayList<JobData>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            list.add(new JobData(key, map.get(key)));
        }
        return list;
    }

    /**
     * 此方法用于关闭dao中 与 hbase 的连接
     *
     * @param table
     */
    public static void close(Table table) {
        try {
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
