package com.example.student_management.Mapper;

import com.example.student_management.entity.Class;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassMapper {
    @Insert("INSERT INTO class (name, major, grade, head_teacher_id) VALUES (#{name}, #{major}, #{grade}, #{headTeacherId})")
    @Options(useGeneratedKeys = true, keyProperty = "classId")
    int insert(Class clazz);

    @Update("UPDATE class SET " +
            "name = #{name}, " +
            "major = #{major}, " +
            "grade = #{grade}, " +
            "head_teacher_id = #{headTeacherId} " +
            "WHERE class_id = #{classId}")
    int update(Class clazz);

    @Delete("DELETE FROM class WHERE class_id = #{classId}")
    int delete(Integer classId);

    List<Class> selectByCondition(@Param("name") String name,
                                  @Param("major") String major,
                                  @Param("grade") Integer grade);

    Class selectById(Integer classId);

    List<Class> selectAll();
}