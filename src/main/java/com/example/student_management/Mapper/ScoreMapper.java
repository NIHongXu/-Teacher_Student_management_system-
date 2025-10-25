package com.example.student_management.Mapper;

import com.example.student_management.entity.Score;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScoreMapper {
    @Insert("INSERT INTO score (student_id, course_id, score, semester) " +
            "VALUES (#{studentId}, #{courseId}, #{score}, #{semester})")
    @Options(useGeneratedKeys = true, keyProperty = "scoreId")
    int insert(Score score);

    @Update("UPDATE score SET " +
            "student_id = #{studentId}, " +
            "course_id = #{courseId}, " +
            "score = #{score}, " +
            "semester = #{semester} " +
            "WHERE score_id = #{scoreId}")
    int update(Score score);

    @Delete("DELETE FROM score WHERE score_id = #{scoreId}")
    int delete(Integer scoreId);


    Score selectById(Integer scoreId);
    @Results({
            @Result(property = "scoreId", column = "score_id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "student.name", column = "student_name"),
            @Result(property = "course.name", column = "course_name")
    })
    List<Score> selectByCondition(@Param("studentId") Integer studentId,
                                  @Param("courseId") Integer courseId,
                                  @Param("semester") String semester,
                                  @Param("minScore") Double minScore,
                                  @Param("maxScore") Double maxScore,
                                  @Param("orderBy") String orderBy,
                                  @Param("order") String order);
}