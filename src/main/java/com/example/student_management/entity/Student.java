package com.example.student_management.entity;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class Student {
    private Integer studentId;
    private String name;
    private String gender;
    private Date birthDate;
    private String phone;
    private String email;
    private String address;
    private Integer classId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;


}