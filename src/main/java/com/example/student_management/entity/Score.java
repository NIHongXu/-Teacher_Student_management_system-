package com.example.student_management.entity;

import lombok.Data;

@Data
public class Score {
    private Integer scoreId;
    private Integer studentId;
    private Integer courseId;
    private Double score;
    private String semester;
    private Student student; // 关联学生
    private Course course; // 关联课程
}
