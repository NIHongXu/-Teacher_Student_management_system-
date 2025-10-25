package com.example.student_management.Service;

import com.example.student_management.entity.Class;
import com.example.student_management.Mapper.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClassService {
    private final ClassMapper classMapper;

    @Autowired
    public ClassService(ClassMapper classMapper) {
        this.classMapper = classMapper;
    }

    public List<Class> getAllClasses() {
        return classMapper.selectAll();
    }

    public Class getClassById(Integer id) {
        return classMapper.selectById(id);
    }

    public boolean addClass(Class clazz) {
        return classMapper.insert(clazz) > 0;
    }

    public boolean updateClass(Class clazz) {
        return classMapper.update(clazz) > 0;
    }

    public boolean deleteClass(Integer id) {
        return classMapper.delete(id) > 0;
    }

    public List<Class> searchClasses(String name, String major, Integer grade) {
        return classMapper.selectByCondition(name, major, grade);
    }
}