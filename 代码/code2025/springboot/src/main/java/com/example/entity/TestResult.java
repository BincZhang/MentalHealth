package com.example.entity;

import lombok.Data;
import java.util.Date;

@Data
public class TestResult {
    private Integer id;
    private Integer userId;
    private String testType;
    private Integer score;
    private String resultLevel;
    private String resultDescription;
    private String resultAdvice;
    private Date testDate;
    private String answers;
} 