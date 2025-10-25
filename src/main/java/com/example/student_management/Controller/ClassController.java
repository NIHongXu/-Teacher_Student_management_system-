package com.example.student_management.Controller;

import com.example.student_management.entity.Class;
import com.example.student_management.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/classes")
public class ClassController {
    private final ClassService classService;

    @Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<List<Class>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Class> getClassById(@PathVariable Integer id) {
        Class clazz = classService.getClassById(id);
        return clazz != null ? ResponseEntity.ok(clazz) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> addClass(@RequestBody Class clazz) {
        return classService.addClass(clazz) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClass(@PathVariable Integer id, @RequestBody Class clazz) {
        clazz.setClassId(id);
        return classService.updateClass(clazz) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Integer id) {
        return classService.deleteClass(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Class>> searchClasses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) Integer grade) {
        return ResponseEntity.ok(classService.searchClasses(name, major, grade));
    }
}
