package com.example.controller;

import com.example.entity.CommunityPost;
import com.example.entity.PostComment;
import com.example.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @GetMapping("/posts")
    public ResponseEntity<List<CommunityPost>> getAllPosts() {
        return ResponseEntity.ok(communityService.getAllPosts());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<CommunityPost> getPostById(@PathVariable Integer id) {
        return ResponseEntity.ok(communityService.getPostById(id));
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody CommunityPost post) {
        communityService.createPost(post);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Integer id) {
        communityService.likePost(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        communityService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<PostComment>> getCommentsByPostId(@PathVariable Integer id) {
        return ResponseEntity.ok(communityService.getCommentsByPostId(id));
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(@RequestBody PostComment comment) {
        communityService.createComment(comment);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        communityService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
} 