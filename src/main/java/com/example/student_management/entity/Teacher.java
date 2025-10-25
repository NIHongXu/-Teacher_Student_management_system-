package com.example.student_management.entity;

import lombok.Data;

@Data
public class Teacher {
    private Integer teacherId;
    private String name;
    private String gender;
    private String title;
    private String department;
}