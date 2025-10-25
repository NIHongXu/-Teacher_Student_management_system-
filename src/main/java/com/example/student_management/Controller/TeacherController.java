package com.example.student_management.Controller;

import com.example.student_management.entity.Teacher;
import com.example.student_management.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin // 解决跨域问题
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Integer id) {
        Teacher teacher = teacherService.getTeacherById(id);
        return teacher != null ? ResponseEntity.ok(teacher) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> addTeacher(@RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTeacher(@PathVariable Integer id, @RequestBody Teacher teacher) {
        teacher.setTeacherId(id);
        return teacherService.updateTeacher(teacher) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }


    @GetMapping("/search")
    public ResponseEntity<List<Teacher>> searchTeachers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(teacherService.searchTeachers(name, department));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer id) {
        return teacherService.deleteTeacher(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

}