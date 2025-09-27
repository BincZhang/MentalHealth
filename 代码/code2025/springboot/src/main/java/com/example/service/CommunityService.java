package com.example.service;

import com.example.entity.CommunityPost;
import com.example.entity.PostComment;
import com.example.mapper.CommunityPostMapper;
import com.example.mapper.PostCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommunityService {
    @Autowired
    private CommunityPostMapper postMapper;

    @Autowired
    private PostCommentMapper commentMapper;

    public List<CommunityPost> getAllPosts() {
        return postMapper.selectAll();
    }

    public CommunityPost getPostById(Integer id) {
        return postMapper.selectById(id);
    }

    @Transactional
    public void createPost(CommunityPost post) {
        postMapper.insert(post);
    }

    @Transactional
    public void likePost(Integer postId) {
        postMapper.incrementLikes(postId);
    }

    @Transactional
    public void deletePost(Integer postId) {
        postMapper.deleteById(postId);
    }

    public List<PostComment> getCommentsByPostId(Integer postId) {
        return commentMapper.selectByPostId(postId);
    }

    @Transactional
    public void createComment(PostComment comment) {
        commentMapper.insert(comment);
        postMapper.incrementComments(comment.getPostId());
    }

    @Transactional
    public void deleteComment(Integer commentId) {
        commentMapper.deleteById(commentId);
    }
} 