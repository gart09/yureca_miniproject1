package model.dto;

import java.time.LocalDateTime;

public class EvaluationDto {
    int evaluationId;
    int studentId;
    int instructorId;
    int behaviorId;
    int studentScore;
    LocalDateTime evaluatedAt;

    public EvaluationDto() {}

    public EvaluationDto(int studentId, int instructorId, int behaviorId, int studentScore, LocalDateTime evaluatedAt) {
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.behaviorId = behaviorId;
        this.studentScore = studentScore;
        this.evaluatedAt = evaluatedAt;
    }

    public EvaluationDto(int evaluationId, int studentId, int instructorId, int behaviorId, int studentScore, LocalDateTime evaluatedAt) {
        this.evaluationId = evaluationId;
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.behaviorId = behaviorId;
        this.studentScore = studentScore;
        this.evaluatedAt = evaluatedAt;
    }

    public int getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(int studentScore) {
        this.studentScore = studentScore;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(int behaviorId) {
        this.behaviorId = behaviorId;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }
}
