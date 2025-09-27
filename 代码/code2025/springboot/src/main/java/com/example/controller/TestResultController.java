package com.example.controller;

import com.example.entity.TestResult;
import com.example.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestResultController {
    @Autowired
    private TestResultService testResultService;

    @PostMapping("/save")
    public ResponseEntity<Void> saveTestResult(@RequestBody TestResult result) {
        testResultService.saveTestResult(result);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/results")
    public ResponseEntity<List<TestResult>> getAllResults() {
        return ResponseEntity.ok(testResultService.getAllResults());
    }

    @GetMapping("/results/{userId}")
    public ResponseEntity<List<TestResult>> getResultsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(testResultService.getByUserId(userId));
    }
} 