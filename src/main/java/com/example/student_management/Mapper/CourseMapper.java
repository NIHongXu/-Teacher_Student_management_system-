package com.example.student_management.Mapper;

import com.example.student_management.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Insert("INSERT INTO course (name, credit, hours, teacher_id) " +
            "VALUES (#{name}, #{credit}, #{hours}, #{teacherId})")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    int insert(Course course);

    @Update("UPDATE course SET " +
            "name = #{name}, " +
            "credit = #{credit}, " +
            "hours = #{hours}, " +
            "teacher_id = #{teacherId} " +
            "WHERE course_id = #{courseId}")
    int update(Course course);

    @Delete("DELETE FROM course WHERE course_id = #{courseId}")
    int delete(Integer courseId);


    List<Course> selectAll();

    List<Course> selectByCondition(String name, Integer teacherId);

    Course selectById(Integer id);
}