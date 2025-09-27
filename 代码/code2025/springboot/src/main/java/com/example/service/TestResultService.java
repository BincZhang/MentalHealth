package com.example.service;

import com.example.entity.TestResult;
import com.example.mapper.TestResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestResultService {
    @Autowired
    private TestResultMapper testResultMapper;

    @Transactional
    public void saveTestResult(TestResult result) {
        testResultMapper.insert(result);
    }

    public List<TestResult> getByUserId(Integer userId) {
        return testResultMapper.selectByUserId(userId);
    }

    public List<TestResult> getAllResults() {
        return testResultMapper.selectAll();
    }
} 