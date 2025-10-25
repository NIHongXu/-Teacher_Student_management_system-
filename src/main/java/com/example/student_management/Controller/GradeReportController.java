package com.example.student_management.Controller;

import com.example.student_management.Service.GradeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

@RequestMapping("/api/reports")
public class GradeReportController {
    private final GradeReportService gradeReportService;

    @Autowired
    public GradeReportController(GradeReportService gradeReportService) {
        this.gradeReportService = gradeReportService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentReport(@PathVariable Integer studentId) {
        return ResponseEntity.ok(gradeReportService.getStudentReport(studentId));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<Map<String, Object>> getClassReport(@PathVariable Integer classId) {
        return ResponseEntity.ok(gradeReportService.getClassReport(classId));
    }
}