package com.yisen.hbaseboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/11 14:25
 */
@Component
@ConfigurationProperties(prefix = "video")
public class VideoConfig {
    private List<Map<String, String>> videos;

    public List<Map<String, String>> getVideos() {
        return videos;
    }

    public void setVideos(List<Map<String, String>> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "VideoConfig{" +
                "video=" + videos +
                '}';
    }
}
