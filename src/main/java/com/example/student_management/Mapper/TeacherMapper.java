package com.example.student_management.Mapper;

import com.example.student_management.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {
    @Insert("INSERT INTO teacher (name, gender, title, department) " +
            "VALUES (#{name}, #{gender}, #{title}, #{department})")
    @Options(useGeneratedKeys = true, keyProperty = "teacherId")
    int insert(Teacher teacher);


    int update(Teacher teacher);



    Teacher selectById(Integer teacherId);


    List<Teacher> selectAll();

    List<Teacher> selectByCondition(String name, String department);

    int delete(Integer id);
}
