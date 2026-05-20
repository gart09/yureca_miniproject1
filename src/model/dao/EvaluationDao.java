package model.dao;

import model.dto.EvaluationDetailDto;
import model.dto.EvaluationDto;

import java.sql.SQLException;
import java.util.List;

public interface EvaluationDao {
    void add(EvaluationDto evaluationDto) throws SQLException;
    void remove(int id) throws SQLException;
    List<EvaluationDetailDto> searchAll() throws SQLException;
    List<EvaluationDetailDto> getStudentAllEvaluation(int studentId) throws SQLException;
    List<EvaluationDetailDto> getInstructorAllEvaluation(int instructorId) throws SQLException;

}