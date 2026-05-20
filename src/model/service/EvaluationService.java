package model.service;

import model.dto.EvaluationDetailDto;
import model.dto.EvaluationDto;

import java.util.List;

public interface EvaluationService {
    void add(int instructorId, int studentId, int behaviorId);
    void remove(int evaluationId);
    List<EvaluationDetailDto> searchAll();
    List<EvaluationDetailDto> getStudentAllEvaluation(int studentId);
    List<EvaluationDetailDto> getInstructorAllEvaluation(int instructorId);
}
