package com.example.student_management.entity;

import lombok.Data;

import java.util.List;

@Data
public class Class {
    private Integer classId;
    private String name;
    private String major;
    private Integer grade;
    private Integer headTeacherId;
    private Teacher headTeacher; // 关联班主任
    private List<Student> students; // 班级学生列表
}