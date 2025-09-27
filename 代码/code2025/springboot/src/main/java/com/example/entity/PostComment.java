package com.example.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PostComment {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private String content;
    private Integer parentId; // 父评论ID，用于回复功能
    private Boolean isAnonymous; // 是否匿名
    private Date createTime;
    private String status; // 状态：正常、已删除等
    
    // 手动添加getter方法
    public Integer getPostId() {
        return this.postId;
    }
} 