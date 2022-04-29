package com.yisen.hbaseboot.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/9 14:36
 */

public class TopDetailsData {
    private Desc desc; //内部类
    private List<JobData> hotRegion;
    private List<Skill> skill;// 内部类 set方式实际为 add方法
    private List<Community> community; // 内部类 set方式实际为 add方法
    private List<Community> video;// 内部类 set方式实际为 add方法
    private List<JobData> other;

    class Desc {
        private String title;
        private String rank;
        private int new1;
        private int total;

        public Desc(String title, String rank, int new1, int total) {
            this.title = title;
            this.rank = rank;
            this.new1 = new1;
            this.total = total;
        }

        @Override
        public String toString() {
            return "Desc{" +
                    "title='" + title + '\'' +
                    ", rank='" + rank + '\'' +
                    ", new=" + new1 +
                    ", total=" + total +
                    '}';
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public int getNew() {
            return new1;
        }

        public void setNew(int new1) {
            this.new1 = new1;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    class Skill {
        private String name;
        private int total;
        private int new1;

        public Skill(String name, int total, int new1) {
            this.name = name;
            this.total = total;
            this.new1 = new1;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getNew() {
            return new1;
        }

        public void setNew(int new1) {
            this.new1 = new1;
        }

        @Override
        public String toString() {
            return "Skill{" +
                    "name='" + name + '\'' +
                    ", total=" + total +
                    ", new=" + new1 +
                    '}';
        }
    }

    class Community {
        private String name;
        private String title;
        private String url;
        private String icon;

        public Community(String name, String title, String url, String icon) {
            this.name = name;
            this.title = title;
            this.url = url;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public void setDesc(String title, String rank, int new1, int total) {
        this.desc = new Desc(title, rank, new1, total);
    }

    public Desc getDesc() {
        return desc;
    }

    public List<JobData> getHotRegion() {
        return hotRegion;
    }

    public void setHotRegion(List<JobData> hotRegion) {
        this.hotRegion = hotRegion;
    }

    public List<Skill> getSkill() {
        return skill;
    }

    public void setSkill(String name, int total, int new1) {
        if (this.skill == null) {
            this.skill = new ArrayList<Skill>();
            this.skill.add(new Skill(name, total, new1));
        } else {
            this.skill.add(new Skill(name, total, new1));
        }
    }

    public List<Community> getCommunity() {
        return community;
    }

    public void setCommunity(String name, String title, String url, String icon) {
        if (this.community == null) {
            this.community = new ArrayList<Community>();
            this.community.add(new Community(name, title, url, icon));
        } else {
            this.community.add(new Community(name, title, url, icon));
        }
    }

    public List<Community> getVideo() {
        return video;
    }

    public void setVideo(String name, String title, String url, String icon) {
        if (this.video == null) {
            this.video = new ArrayList<Community>();
            this.video.add(new Community(name, title, url, icon));
        } else {
            this.video.add(new Community(name, title, url, icon));
        }
    }

    public List<JobData> getOther() {
        return other;
    }

    public void setOther(List<JobData> other) {
        this.other = other;
    }
}
