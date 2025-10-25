package com.example.student_management.Mapper;


import com.example.student_management.entity.GithubCommit;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface GithubCommitMapper {


    int insert(GithubCommit commit);

    List<GithubCommit> selectByStudentId(Integer studentId);


}