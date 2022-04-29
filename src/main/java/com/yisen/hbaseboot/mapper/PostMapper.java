package com.yisen.hbaseboot.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/26 12:59
 */
@Mapper
public interface PostMapper {
    int addSinglePosition(Map map);
}
