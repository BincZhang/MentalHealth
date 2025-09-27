package com.example.mapper;

import com.example.entity.TestResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestResultMapper {
    int insert(TestResult result);
    List<TestResult> selectByUserId(Integer userId);
    List<TestResult> selectAll();
} 