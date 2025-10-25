package com.example.student_management.Service;

import com.example.student_management.entity.Teacher;
import com.example.student_management.Mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherService {
    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherService(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    public List<Teacher> getAllTeachers() {
        return teacherMapper.selectAll();
    }

    public Teacher getTeacherById(Integer id) {
        return teacherMapper.selectById(id);
    }

    public boolean addTeacher(Teacher teacher) {
        return teacherMapper.insert(teacher) > 0;
    }

    public boolean updateTeacher(Teacher teacher) {
        return teacherMapper.update(teacher) > 0;
    }


    public List<Teacher> searchTeachers(String name, String department) {
        return teacherMapper.selectByCondition(name, department);
    }

    public boolean deleteTeacher(Integer id) {
        return teacherMapper.delete(id) > 0;
    }
}