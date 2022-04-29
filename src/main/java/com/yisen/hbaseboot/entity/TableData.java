package com.yisen.hbaseboot.entity;

import lombok.Data;

/**
 * @AuthorList: LiuYiSen, LuWei
 * @Date: 2020/6/12 16:45
 */
@Data
public class TableData {
    private int index;
    private String date;
    private String location;
    private int locationNum;
    private String locationSalary;
    private String locationHotLocation;
    private String locationCompanySize;
    private String locationBenefit;
    private String position;
    private int positionNum;
    private String positionSalary;
    private String positionHotLocation;
    private String positionCompanySize;
    private String positionBenefit;

}
