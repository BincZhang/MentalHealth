package com.example.mapper;

import com.example.entity.PostComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostCommentMapper {
    @Insert("INSERT INTO post_comment (post_id, user_id, content, parent_id, is_anonymous, create_time, status) " +
            "VALUES (#{postId}, #{userId}, #{content}, #{parentId}, #{isAnonymous}, NOW(), 'normal')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PostComment comment);

    @Select("SELECT c.*, u.username as userName FROM post_comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.post_id = #{postId} AND c.status = 'normal' " +
            "ORDER BY c.create_time ASC")
    List<PostComment> selectByPostId(@Param("postId") Integer postId);

    @Update("UPDATE post_comment SET status = 'deleted' WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);
} 