package com.yisen.hbaseboot.entity;

import lombok.Data;

import java.util.List;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/6 11:44
 * 封装了学历相关的结果
 */
@Data
public class EduData {
    private List<JobData> position;
    private List<JobData> jc;
    private List<JobData> rcc;

}
