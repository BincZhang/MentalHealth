package com.example.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CommunityPost {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private String category; // 帖子分类：心情分享、求助、经验交流等
    private Integer likes; // 点赞数
    private Integer comments; // 评论数
    private Boolean isAnonymous; // 是否匿名
    private Date createTime;
    private Date updateTime;
    private String status; // 状态：正常、已删除等
} 