package com.example.student_management.Service;

import com.example.student_management.entity.*;
import com.example.student_management.Mapper.*;
import com.example.student_management.entity.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GradeReportService {
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;
    private final ScoreMapper scoreMapper;
    private final ClassMapper classMapper;
    private final TeacherMapper teacherMapper;

    @Autowired
    public GradeReportService(StudentMapper studentMapper, CourseMapper courseMapper,
                              ScoreMapper scoreMapper, ClassMapper classMapper,
                              TeacherMapper teacherMapper) {
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
        this.scoreMapper = scoreMapper;
        this.classMapper = classMapper;
        this.teacherMapper = teacherMapper;
    }

    // 获取学生成绩单
    public Map<String, Object> getStudentReport(Integer studentId) {
        Map<String, Object> report = new HashMap<>();

        // 学生基本信息
        Student student = studentMapper.selectById(studentId);
        report.put("student", student);

        // 班级信息
        if (student.getClassId() != null) {
            Class clazz = classMapper.selectById(student.getClassId());
            report.put("class", clazz);
        }

        // 成绩信息
        List<Score> scores = scoreMapper.selectByCondition(studentId, null, null, null, null, null, null);
        List<Map<String, Object>> scoreDetails = new ArrayList<>();

        for (Score score : scores) {
            Map<String, Object> detail = new HashMap<>();
            Course course = courseMapper.selectById(score.getCourseId());

            detail.put("course", course);
            detail.put("score", score.getScore());
            detail.put("semester", score.getSemester());

            scoreDetails.add(detail);
        }

        report.put("scores", scoreDetails);

        // 计算平均分
        if (!scores.isEmpty()) {
            double average = scores.stream()
                    .mapToDouble(Score::getScore)
                    .average()
                    .orElse(0.0);
            report.put("average", average);
        }

        return report;
    }

    // 获取班级成绩统计
    public Map<String, Object> getClassReport(Integer classId) {
        Map<String, Object> report = new HashMap<>();

        // 班级信息
        Class clazz = classMapper.selectById(classId);
        report.put("class", clazz);

        // 班级学生
        List<Student> students = studentMapper.selectByClassId(classId);
        report.put("students", students);

        // 班级课程
        List<Course> courses = courseMapper.selectAll(); // 可根据实际情况调整

        // 各科成绩统计
        List<Map<String, Object>> courseStats = new ArrayList<>();

        for (Course course : courses) {
            Map<String, Object> stat = new HashMap<>();
            List<Score> scores = scoreMapper.selectByCondition(null, course.getCourseId(), null, null, null, null, null);

            if (!scores.isEmpty()) {
                stat.put("course", course);

                // 计算平均分、最高分、最低分
                double average = scores.stream()
                        .mapToDouble(Score::getScore)
                        .average()
                        .orElse(0.0);
                double max = scores.stream()
                        .mapToDouble(Score::getScore)
                        .max()
                        .orElse(0.0);
                double min = scores.stream()
                        .mapToDouble(Score::getScore)
                        .min()
                        .orElse(0.0);

                stat.put("average", average);
                stat.put("max", max);
                stat.put("min", min);

                courseStats.add(stat);
            }
        }

        report.put("courseStats", courseStats);

        return report;
    }
}