package com.example.student_management.Service;

import com.example.student_management.entity.Score;
import com.example.student_management.Mapper.ScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScoreService {
    private final ScoreMapper scoreMapper;

    @Autowired
    public ScoreService(ScoreMapper scoreMapper) {
        this.scoreMapper = scoreMapper;
    }

    public List<Score> getAllScores() {
        return scoreMapper.selectByCondition(null, null, null, null, null, null, null);
    }

    public Score getScoreById(Integer id) {
        return scoreMapper.selectById(id);
    }

    public boolean addScore(Score score) {
        return scoreMapper.insert(score) > 0;
    }

    public boolean updateScore(Score score) {
        return scoreMapper.update(score) > 0;
    }

    public boolean deleteScore(Integer id) {
        return scoreMapper.delete(id) > 0;
    }

    public List<Score> searchScores(Integer studentId, Integer courseId, String semester,
                                    Double minScore, Double maxScore, String orderBy, String order) {
        return scoreMapper.selectByCondition(studentId, courseId, semester, minScore, maxScore, orderBy, order);
    }

    public List<Score> getScoresByStudentId(Integer studentId) {
        return scoreMapper.selectByCondition(studentId, null, null, null, null, null, null);
    }

    public List<Score> getScoresByCourseId(Integer courseId) {
        return scoreMapper.selectByCondition(null, courseId, null, null, null, null, null);
    }
}
