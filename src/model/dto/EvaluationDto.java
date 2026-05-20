package model.dto;

import java.time.LocalDateTime;

public class EvaluationDto {
    int evaluationId;
    int studentId;
    int instructorId;
    int behaviorId;
    LocalDateTime evaluatedAt;

    public EvaluationDto() {}

    public EvaluationDto(int studentId, int instructorId, int behaviorId, LocalDateTime evaluatedAt) {
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.behaviorId = behaviorId;
        this.evaluatedAt = evaluatedAt;
    }

    public EvaluationDto(int evaluationId, int studentId, int instructorId, int behaviorId, LocalDateTime evaluatedAt) {
        this.evaluationId = evaluationId;
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.behaviorId = behaviorId;
        this.evaluatedAt = evaluatedAt;
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
