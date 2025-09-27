package com.example.mapper;

import com.example.entity.CommunityPost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommunityPostMapper {
    @Insert("INSERT INTO community_post (user_id, title, content, category, likes, comments, is_anonymous, create_time, update_time, status) " +
            "VALUES (#{userId}, #{title}, #{content}, #{category}, 0, 0, #{isAnonymous}, NOW(), NOW(), 'normal')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CommunityPost post);

    @Select("SELECT p.*, u.username as userName FROM community_post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "WHERE p.status = 'normal' " +
            "ORDER BY p.create_time DESC")
    List<CommunityPost> selectAll();

    @Select("SELECT p.*, u.username as userName FROM community_post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "WHERE p.id = #{id} AND p.status = 'normal'")
    CommunityPost selectById(@Param("id") Integer id);

    @Update("UPDATE community_post SET likes = likes + 1 WHERE id = #{id}")
    int incrementLikes(@Param("id") Integer id);

    @Update("UPDATE community_post SET comments = comments + 1 WHERE id = #{id}")
    int incrementComments(@Param("id") Integer id);

    @Update("UPDATE community_post SET status = 'deleted' WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);
} 