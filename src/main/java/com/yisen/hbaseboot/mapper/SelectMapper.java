package com.yisen.hbaseboot.mapper;

import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.entity.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/27 11:41
 */
@Mapper
public interface SelectMapper {
    /**
     * 获取单条职位信息
     *
     * @param pid positionName字段
     * @return
     */
    Position getPosDetail(@Param("pid") String pid);

    /**
     * 获取所有数据分页查询
     *
     * @return
     */
    List<Position> getPosList();

    /**
     * 根据条件获取所有数据分页查询
     */
    List<Map<String, String>> searchPos(@Param("key") String key,
                                        @Param("location") String location,
                                        @Param("map") Map map,
                                        @Param("leftSalary") String leftSalary,
                                        @Param("rightSalary") String rightSalary);
}
