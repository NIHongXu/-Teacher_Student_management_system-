package com.example.student_management.entity;

import lombok.Data;

import java.time.LocalDateTime;


// GithubCommit.java
@Data
public class GithubCommit {
    private String sha;
    private String message;
    private String author;
    private Integer id;
    private Integer studentId;
    private String repoUrl;
    private String commitHash;
    private String commitMessage;
    private LocalDateTime commitDate;
    private Integer linesAdded;
    private Integer linesDeleted;

    // 关联学生信息
    private Student student;
}
