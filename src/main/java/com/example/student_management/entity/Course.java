package com.example.student_management.entity;

import lombok.Data;

@Data
public class Course {
    private Integer courseId;
    private String name;
    private Integer credit;
    private Integer hours;
    private Integer teacherId;
    private Teacher teacher; // 关联教师
}
