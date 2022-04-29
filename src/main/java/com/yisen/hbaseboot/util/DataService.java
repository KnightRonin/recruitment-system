package com.yisen.hbaseboot.util;

import java.util.*;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/6 15:20
 */
public class DataService {

    /**
     * 此方法用来将参数map 以其值(Integer类型) 来排序
     *
     * @param map 要以值来排序的map集合
     * @param n   保留长度，0为全保留
     * @return
     */
    public static Map<String, Integer> getValueSortMap(Map<String, Integer> map, int n) {
        Set<String> set = map.keySet();
        ArrayList<String> list = new ArrayList<>(set);
        // 长度标记
        int index = 0;
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return map.get(o2) - map.get(o1);
            }
        });
        Map<String, Integer> linkMap = new LinkedHashMap<>();
        for (String s : list) {
            linkMap.put(s, map.get(s));
            index++;
            if (index >= n & n != 0)
                break;
        }
        return linkMap;
    }

    /**
     * 将薪资处理为 double形式
     *
     * @param salary
     * @return
     */
    public static Double getSalaryDouble(String salary) {
        double salary_d = 0.0;
        if (salary.contains("k")) {
            salary_d = Double.parseDouble(salary.substring(0, salary.indexOf("k")));
        } else if (salary.contains("K")) {
            salary_d = Double.parseDouble(salary.substring(0, salary.indexOf("K")));
        }
        return salary_d;
    }

    /**
     * 数据库结果集，按照传入的列 进行分组数量统计
     *
     * @param list
     * @param key
     * @return
     */
    public static Map<String, Integer> columnClassSum(List<Map<String, String>> list, String key) {
        Map<String, Integer> hashMap = new HashMap<>();
        for (Map<String, String> map : list) {
            String value = map.get(key);
            if (hashMap.containsKey(value)) {
                hashMap.put(value, hashMap.get(value) + 1);
            } else {
                hashMap.put(value, 1);
            }
        }

        return hashMap;
    }

    /**
     * 该方法将字符串形式的数组转化为字符串数组
     *
     * @param str 字符串
     * @return 返回转化完的数组
     */
    public static List<String> parseStrArray(String str) {
        String substr = str.substring(2, str.length() - 2);
        String[] split = substr.split("\",\"");
        return Arrays.asList(split);
    }
}
