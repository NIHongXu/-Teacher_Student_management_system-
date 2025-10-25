package com.example.student_management.Controller;

import com.example.student_management.entity.Score;
import com.example.student_management.Service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin // 解决跨域问题
public class ScoreController {
    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping
    public ResponseEntity<List<Score>> getAllScores() {
        return ResponseEntity.ok(scoreService.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Score> getScoreById(@PathVariable Integer id) {
        Score score = scoreService.getScoreById(id);
        return score != null ? ResponseEntity.ok(score) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> addScore(@RequestBody Score score) {
        return scoreService.addScore(score) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateScore(@PathVariable Integer id, @RequestBody Score score) {
        score.setScoreId(id);
        return scoreService.updateScore(score) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Integer id) {
        return scoreService.deleteScore(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Score>> searchScores(
            @RequestParam(required = false) Integer studentId,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) Double minScore,
            @RequestParam(required = false) Double maxScore,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String order) {
        return ResponseEntity.ok(scoreService.searchScores(
                studentId, courseId, semester, minScore, maxScore, orderBy, order));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Score>> getScoresByStudentId(@PathVariable Integer studentId) {
        return ResponseEntity.ok(scoreService.getScoresByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Score>> getScoresByCourseId(@PathVariable Integer courseId) {
        return ResponseEntity.ok(scoreService.getScoresByCourseId(courseId));
    }
}