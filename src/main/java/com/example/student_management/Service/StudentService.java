package com.example.student_management.Service;

import com.example.student_management.entity.Student;
import com.example.student_management.Mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {
    private final StudentMapper studentMapper;


    @Autowired
    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public List<Student> getAllStudents() {
        return studentMapper.selectAll();
    }

    public Student getStudentById(Integer id) {
        return studentMapper.selectById(id);
    }

    public boolean addStudent(Student student) {
        return studentMapper.insert(student) > 0;
    }

    public boolean updateStudent(Student student) {
        return studentMapper.update(student) > 0;
    }

    public List<Student> searchStudents(String name, Integer classId, String gender) {
        return studentMapper.selectByCondition(name, classId, gender);
    }

    public List<Student> getStudentsByClassId(Integer classId) {
        return studentMapper.selectByClassId(classId);
    }

    public boolean deleteStudent(Integer id) {
        return studentMapper.delete(id) > 0;
    }

    public List<String> getAllClassNames() {
        return studentMapper.selectDistinctClasses();
    }
}