package com.yisen.hbaseboot.entity;

import lombok.Data;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/27 9:31
 */
@Data
public class Position {
    private int positionId;
    private String positionName;
    private int companyId;
    private String salary;
    private String city;
    private String city_province;
    private String district;
    private String workYear;
    private String education;
    private String jobNature;
    private String positionLables;
    private String companyLabelList;
    private String skillLables;
    private String companyFullName;
    private String companyShortName;
    private String famousCompany;
    private String industryField;
    private String financeStage;
    private String companySize;
    private String companyLogo;
    private String longitude;
    private String latitude;
    private String firstType;
    private String secondType;
    private String thirdType;
    private String createTime;
    private String positionDesc;
}
