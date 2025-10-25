package com.example.student_management.Service;

import com.example.student_management.entity.Course;
import com.example.student_management.Mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {
    private final CourseMapper courseMapper;

    @Autowired
    public CourseService(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    public List<Course> getAllCourses() {
        return courseMapper.selectAll();
    }

    public Course getCourseById(Integer id) {
        return courseMapper.selectById(id);
    }

    public boolean addCourse(Course course) {
        return courseMapper.insert(course) > 0;
    }

    public boolean updateCourse(Course course) {
        return courseMapper.update(course) > 0;
    }

    public boolean deleteCourse(Integer id) {
        return courseMapper.delete(id) > 0;
    }

    public List<Course> searchCourses(String name, Integer teacherId) {
        return courseMapper.selectByCondition(name, teacherId);
    }

    public List<Course> getCoursesByTeacherId(Integer teacherId) {
        return courseMapper.selectByCondition(null, teacherId);
    }
}