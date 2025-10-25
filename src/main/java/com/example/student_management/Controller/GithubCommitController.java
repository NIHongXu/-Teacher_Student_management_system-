package com.example.student_management.Controller;

import com.example.student_management.Service.GithubCommitService;
import com.example.student_management.entity.GithubCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// GithubCommitController.java
@RestController
@CrossOrigin
@RequestMapping("/api/github-commits")
public class GithubCommitController {

    @Autowired
    private GithubCommitService commitService;

    @PostMapping("/sync/{studentId}")
    public ResponseEntity<?> syncCommits(
            @PathVariable Integer studentId,
            @RequestParam String githubUsername) {

        commitService.syncStudentCommits(studentId, githubUsername);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{studentId}")
    public List<GithubCommit> getCommits(@PathVariable Integer studentId) {
        return commitService.getStudentCommits(studentId);
    }
}