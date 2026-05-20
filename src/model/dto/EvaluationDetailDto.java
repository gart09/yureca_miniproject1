package model.dto;

import java.time.LocalDateTime;

public class EvaluationDetailDto {
    String          instructorName;
    String          studentName;
    int             studentScore;
    String          behaviorName;
    int             behaviorScore;
    LocalDateTime   evaluated_at;

    public EvaluationDetailDto(String instructorName, String studentName, int studentScore, String behaviorName, int behaviorScore, LocalDateTime evaluated_at) {
        this.instructorName = instructorName;
        this.studentName = studentName;
        this.studentScore = studentScore;
        this.behaviorName = behaviorName;
        this.behaviorScore = behaviorScore;
        this.evaluated_at = evaluated_at;
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

    public LocalDateTime getEvaluated_at() {
        return evaluated_at;
    }

    public void setEvaluated_at(LocalDateTime evaluated_at) {
        this.evaluated_at = evaluated_at;
    }
}
