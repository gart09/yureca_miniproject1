package model.dto;

import java.time.LocalDateTime;

public class EvaluationDetailDto {
    int             evaluationId;
    String          instructorName;
    String          studentName;
    int             studentScore;
    String          behaviorName;
    int             behaviorScore;
    LocalDateTime   evaluatedAt;

    public EvaluationDetailDto() {}


    public EvaluationDetailDto(int evaluationId, String instructorName, String studentName, int studentScore, String behaviorName, int behaviorScore, LocalDateTime evaluatedAt) {
        this.evaluationId = evaluationId;
        this.instructorName = instructorName;
        this.studentName = studentName;
        this.studentScore = studentScore;
        this.behaviorName = behaviorName;
        this.behaviorScore = behaviorScore;
        this.evaluatedAt = evaluatedAt;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(int studentScore) {
        this.studentScore = studentScore;
    }

    public String getBehaviorName() {
        return behaviorName;
    }

    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName;
    }

    public int getBehaviorScore() {
        return behaviorScore;
    }

    public void setBehaviorScore(int behaviorScore) {
        this.behaviorScore = behaviorScore;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluated_at) {
        this.evaluatedAt = evaluatedAt;
    }
}
