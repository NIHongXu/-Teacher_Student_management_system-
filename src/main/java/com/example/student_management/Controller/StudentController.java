package com.example.student_management.Controller;

import com.example.student_management.entity.Student;
import com.example.student_management.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin // 解决跨域问题
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }
    @GetMapping
    public ResponseEntity<List<Student>> getStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer classId,  // 添加classId参数
            @RequestParam(required = false) String gender
    ) {
        List<Student> students;
        if (name != null || classId != null || gender != null) {
            students = studentService.searchStudents(name, classId, gender);
        } else {
            students = studentService.getAllStudents();
        }
        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<Void> addStudent(@RequestBody Student student) {
        return studentService.addStudent(student) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        student.setStudentId(id);
        return studentService.updateStudent(student) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        return studentService.deleteStudent(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer classId,
            @RequestParam(required = false) String gender) {
        return ResponseEntity.ok(studentService.searchStudents(name, classId, gender));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Student>> getStudentsByClassId(@PathVariable Integer classId) {
        return ResponseEntity.ok(studentService.getStudentsByClassId(classId));
    }
    @GetMapping("/classes")
    public ResponseEntity<List<String>> getAllClasses() {
        List<String> classes = studentService.getAllClassNames(); // 注意返回String列表
        return ResponseEntity.ok(classes);
    }
}