package com.example.student_management.Mapper;

import com.example.student_management.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Options(useGeneratedKeys = true, keyProperty = "studentId")
    int insert(Student student);


    int update(Student student);


    Student selectById(Integer studentId);

    List<Student> selectAll();

    List<Student> selectByCondition(@Param("name") String name,
                                    @Param("classId") Integer classId,
                                    @Param("gender") String gender);

    List<Student> selectByClassId(Integer classId);

    int  delete(Integer id);

    List<String> selectDistinctClasses();
}