package com.yisen.hbaseboot.service.impl;

import com.yisen.hbaseboot.config.CommunityConfig;
import com.yisen.hbaseboot.config.VideoConfig;
import com.yisen.hbaseboot.dao.AllCountryJobDao;
import com.yisen.hbaseboot.entity.TopDetailsData;
import com.yisen.hbaseboot.util.DataService;
import com.yisen.hbaseboot.entity.EduData;
import com.yisen.hbaseboot.entity.JobData;
import com.yisen.hbaseboot.service.AllCountryJobService;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.ResultService;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @AuthorList: LiuYiSen, LuWei
 * @Date: 2020/6/4 19:20
 */
@Service
public class AllCountryJobServiceImpl implements AllCountryJobService {
    @Autowired
    AllCountryJobDao allCountryJobDao;
    @Autowired
    CommunityConfig communityConfig;
    @Autowired
    VideoConfig videoConfig;
    @Autowired
    String family;

    /**
     * /AllProvince_DayJob_Servlet接口  业务逻辑方法
     *
     * @param date
     * @return
     * @throws DaoException
     */
    @Override
    public List<JobData> getCountryEveryDay(String date) throws DaoException {
        Map<String, Integer> map = allCountryJobDao.getCountryEveryDay(date, family, "createTime");
        return ResultService.parseList(map);
    }

    /**
     * /CountryJobEdu_Top_Servlet接口 业务逻辑方法
     *
     * @return
     * @throws DaoException
     */
    @Override
    public EduData getEducationPosition() throws DaoException {
        List<Map<String, Integer>> list = allCountryJobDao.getEducationPosition(family, "education", "secondType");
        // 封装 EduData 中的 jc 和 rcc 属性
        EduData eduData = new EduData();
        Map<String, Integer> map0 = list.get(0);
        Map<String, Integer> map1 = list.get(1);
        eduData.setJc(ResultService.parseList(map0));
        eduData.setRcc(ResultService.parseList(map1));
        // 封装EduData中的 position 属性
        Map<String, Integer> position = new TreeMap<>();
        Set<String> key0 = map0.keySet();
        //      取得所有 key
        HashSet<String> keys = new HashSet<String>(map1.keySet());
        for (String s : key0) {
            keys.add(s);
        }
        for (String key : keys) {
            position.put(key, (map0.containsKey(key) ? map0.get(key) : 0) +
                    (map1.containsKey(key) ? map1.get(key) : 0));
        }
        // 将map 集合 以值(Integer类型)  来排序
        position = DataService.getValueSortMap(position, 13);
        eduData.setPosition(ResultService.parseList(position));
        return eduData;
    }

    /**
     * /CountryJob_DayTop_Servlet 接口
     * 该接口返回指定日期[date] 年月或所有时间[type] 相对职位[position] 在不同地区[region] 的详情
     *
     * @param date     查询的时间 2020-5-11
     * @param type     查询的时间类型 Day Month Year All
     * @param position 查询的职位名称 后端开发
     * @param level    查询的下级城市等级   1 省 2 市 3 区
     * @param region   查询的区域 广东省
     * @return
     * @throws ServiceException
     * @throws DaoException
     */
    @Override
    public TopDetailsData getTopDetails(String date, String type, String position, Integer level, String region) throws ServiceException, DaoException {
        TopDetailsData detailsData = new TopDetailsData();
        String position_blak = position;
        if (date == null || date.equals("")) {
            date = LocalDate.now().toString();
        }
        int index = date.indexOf("-");
        String year = date.substring(0, index);
        String month = date.substring(index + 1, date.indexOf("-", index + 1));
        String day = date.substring(date.indexOf("-", index + 1) + 1);
        LocalDate localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
//        date = date.replace("-", "/");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d");
//        Calendar calendar = Calendar.getInstance();
        // 日期和时间区域参数的处理
        String date0 = "";
        String date1 = "";
        if (date != null & type != null) {
            if (type.equals("Day")) {
                date1 = localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth();
                localDate = localDate.plusDays(-1);
                date0 = localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth();
            } else if (type.equals("Month")) {
                date1 = localDate.getYear() + "/" + localDate.getMonthValue();
                localDate = localDate.plusMonths(-1);
                date0 = localDate.getYear() + "/" + localDate.getMonthValue();
            } else if (type.equals("Year")) {
                date1 = localDate.getYear() + "/";
                localDate = localDate.plusYears(-1);
                date0 = localDate.getYear() + "/";
            }
        }
        // position 职位名称的参数处理
        String[] secondType = {"后端开发", "测试", "人工智能", "移动前端开发", "运维", "数据开发", "前端开发", "高端技术职位", "项目管理", "硬件开发", "企业软件",
                "产品经理", "运营"};
        List<String> list = Arrays.asList(secondType);
        if (!list.contains(position) & !position.equals("")) {
            throw new ServiceException("职位名称错误！");
        }

        //dao 结果 前一天和后一天
        List<Map<String, String>> topDetails0 = null;
        List<Map<String, String>> topDetails1 = null;
//        System.out.println(position);
        List<String> municipalities = Arrays.asList("上海市", "北京市", "天津市", "重庆市");
        if (municipalities.contains(region)) {
            level = 3;
        }
        // 根据下级返回省市区决定查询列
        if (level == 1) { //返回下级省
//            System.out.println(date0+"----"+date1);
            topDetails0 = allCountryJobDao.getTopDetails(family, date0, secondType, null, "createTime", "secondType", "positionLables", "city_province");
            topDetails1 = allCountryJobDao.getTopDetails(family, date1, secondType, null, "createTime", "secondType", "positionLables", "city_province");
//            System.out.println(topDetails0==topDetails1);
//            System.out.println(topDetails0.equals(topDetails1));
        } else if (level == 2) {//返回下级市
            if (region != null & !region.equals("")) {
                region = region.substring(0, 2);
                topDetails0 = allCountryJobDao.getTopDetails(family, date0, secondType, region, "createTime", "secondType", "positionLables", "city_province", "city");
                topDetails1 = allCountryJobDao.getTopDetails(family, date1, secondType, region, "createTime", "secondType", "positionLables", "city_province", "city");
            } else {
                topDetails0 = allCountryJobDao.getTopDetails(family, date0, secondType, null, "createTime", "secondType", "positionLables", "city_province", "city");
                topDetails1 = allCountryJobDao.getTopDetails(family, date1, secondType, null, "createTime", "secondType", "positionLables", "city_province", "city");
            }
        } else if (level == 3) {//返回下级区
            if (region != null & !region.equals("")) {
                region = region.substring(0, 2);
                topDetails0 = allCountryJobDao.getTopDetails(family, date0, secondType, region, "createTime", "secondType", "positionLables", "city", "district");
                topDetails1 = allCountryJobDao.getTopDetails(family, date1, secondType, region, "createTime", "secondType", "positionLables", "city", "district");
            } else {
                topDetails0 = allCountryJobDao.getTopDetails(family, date0, secondType, null, "createTime", "secondType", "positionLables", "city", "district");
                topDetails1 = allCountryJobDao.getTopDetails(family, date1, secondType, null, "createTime", "secondType", "positionLables", "city", "district");
            }
        }
        //------------------------------------ dao 返回结果 业务处理和封装-----------------------------------
        // 所有职位名称和数量的排名
        Map<String, Integer> positionMap = new LinkedHashMap<String, Integer>();
        for (String s : secondType) {
            positionMap.put(s, 0);
        }
        for (Map<String, String> map : topDetails1) {
            String key = map.get("secondType");
            if (positionMap.containsKey(key)) {
                positionMap.put(key, positionMap.get(key) + 1);
            }
        }
        positionMap = DataService.getValueSortMap(positionMap, 0);
        // 全部
        int desc_total = 0;
        //职位排名
        String desc_rank = "TOP";
        // position_blak 是要统计详情的职位名称
        // 确定要统计的职位、排名
        int n = 1;
        for (String s : positionMap.keySet()) {
            if (s.equals(position_blak)) {
                desc_rank += n;
                break;
            } else if (position_blak.equals("") | position_blak == null) {
                position_blak = s;
                desc_rank += n;
                break;
            } else {
                n++;
            }
        }

        //存放最多的下级区域
        Map<String, Integer> levelMap = new LinkedHashMap<String, Integer>();
        //存放技术要求数量信息
        Map<String, Integer> skillMap = new LinkedHashMap<String, Integer>();
        // 确定要查询的具体职位后 进行二次详细信息统计
        for (Map<String, String> map : topDetails1) {
            if (map.get("secondType").equals(position_blak)) {
                // 计算总数
                desc_total++;
                // 统计下级区域职位数量信息
                String levelKey = "";
                if (level == 1)
                    levelKey = map.get("city_province");
                else if (level == 2)
                    levelKey = map.get("city");
                else if (level == 3)
                    levelKey = map.get("district");
                if (levelMap.containsKey(levelKey)) {
                    levelMap.put(levelKey, levelMap.get(levelKey) + 1);
                } else {
                    levelMap.put(levelKey, 1);
                }
                // 统计所使用技术的总数
                String skillKey = map.get("positionLables");

                if (skillKey != null && !skillKey.equals("[]")) {
                    skillKey = skillKey.substring(2, skillKey.indexOf("]") - 1);

                    String[] skillKeys = skillKey.split("', '");
                    for (String key : skillKeys) {
                        if (skillMap.containsKey(key)) {
                            skillMap.put(key, skillMap.get(key) + 1);
                        } else {
                            skillMap.put(key, 1);
                        }
                    }
                }
            }
        }
        // -----------昨日、上个月、去年的数量统计，计算new------------------
        int old_desc_total = 0;
        Map<String, Integer> old_skillMap = new LinkedHashMap<String, Integer>();
        for (String s : skillMap.keySet()) {
            old_skillMap.put(s, 0);
        }
        for (Map<String, String> map : topDetails0) {
            if (map.get("secondType").equals(position_blak)) {
                // 计算总数
                old_desc_total++;
                // 统计所使用技术的总数
                String skillKey = map.get("positionLables");
//                System.out.println(map);
                if (skillKey != null && !skillKey.equals("[]")) {
                    skillKey = skillKey.substring(2, skillKey.indexOf("]") - 1);

                    String[] skillKeys = skillKey.split("', '");
                    for (String key : skillKeys) {
                        if (old_skillMap.containsKey(key)) {
                            old_skillMap.put(key, old_skillMap.get(key) + 1);
                        }
                    }
                }
            }
        }

        //结果封装
        positionMap.remove(position_blak);
        detailsData.setOther(ResultService.parseList(positionMap));
        detailsData.setDesc(position_blak, desc_rank, desc_total - old_desc_total, desc_total);
        detailsData.setHotRegion(ResultService.parseList(
                DataService.getValueSortMap(levelMap, 3)));
        for (Object o : DataService.getValueSortMap(skillMap, 15).keySet()) {
            String key = (String) o;
            int skillTotal = skillMap.get(key);
            int old_skillTotal = old_skillMap.get(key);
            detailsData.setSkill(key, skillTotal, skillTotal - old_skillTotal);
        }
        // yml配置文件常量封装
        for (Map<String, String> map : communityConfig.getCommunities()) {
            detailsData.setCommunity(map.get("name"), map.get("title"),
                    map.get("url"), map.get("icon"));
        }
        for (Map<String, String> map : videoConfig.getVideos()) {
            detailsData.setVideo(map.get("name"), map.get("title"),
                    map.get("url"), map.get("icon"));
        }

        detailsData.setOther(ResultService.parseList(positionMap));
        return detailsData;
    }

    /**
     * /CounterCompanyScale_Servlet 接口
     * 返回公司规模不同的公司分组数量情况
     *
     * @return
     */
    @Override
    public List<JobData> getCompanyScale() throws DaoException {
        Map<String, Integer> map = allCountryJobDao.getCompanyScale(family, "companySize");
        List<JobData> jobData = ResultService.parseList(map);
        return jobData;
    }

    /**
     * /CountryJobWelfare_Servlet 接口
     * 全国公司福利情况统计
     *
     * @return
     */
    @Override
    public List<JobData> getCompanyWelfare() throws DaoException {
        Map<String, Integer> map = allCountryJobDao.getCompanyWelfare(family, "companyLabelList");
        return ResultService.parseList(map);
    }
}
