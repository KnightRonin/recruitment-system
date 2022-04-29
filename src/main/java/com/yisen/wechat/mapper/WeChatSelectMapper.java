package com.yisen.wechat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/8/5 9:47
 */
@Mapper
public interface WeChatSelectMapper {
    List<Map<String, String>> matchPos(@Param("leftSalary") String leftSalary,
                                       @Param("rightSalary") String rightSalary,
                                       @Param("citys") List<String> citys,
                                       @Param("positions") List<Map<String, String>> positions,
                                       @Param("labels") List<String> labels,
                                       @Param("extraIds") List<String> extraIds,
                                       @Param("jobNature") String jobNature);
}
