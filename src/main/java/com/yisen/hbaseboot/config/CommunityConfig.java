package com.yisen.hbaseboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/6/11 14:23
 */
@Component
@ConfigurationProperties(prefix = "community")
public class CommunityConfig {
    private List<Map<String, String>> communities;

    public List<Map<String, String>> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Map<String, String>> communities) {
        this.communities = communities;
    }

    @Override
    public String toString() {
        return "CommunityConfig{" +
                "community=" + communities +
                '}';
    }
}
