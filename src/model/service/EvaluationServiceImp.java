package model.service;

import model.dao.*;
import model.dto.BehaviorDto;
import model.dto.EvaluationDetailDto;
import model.dto.EvaluationDto;
import model.dto.StudentDto;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class EvaluationServiceImp implements EvaluationService {
    private EvaluationDao dao = new EvaluationDaoImp();

    @Override
    public void add(int instructorId, int studentId, int behaviorId) {
        StudentDao studentDao = new StudentDaoImp();
        BehaviorDao behaviorDao = new BehaviorDaoImp();
        try {
            LocalDateTime now = LocalDateTime.now();
            int studentScore = studentDao.searchById(studentId).getScore();
            EvaluationDto evaluationDto = new EvaluationDto(instructorId, studentId, behaviorId, studentScore, now);
            dao.add(evaluationDto);

            StudentDto studentDto = studentDao.searchById(studentId);
            BehaviorDto behaviorDto = behaviorDao.searchById(behaviorId);
            studentDto.setScore(studentDto.getScore() + behaviorDto.getScore());
            studentDao.update(studentDto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void remove(int evaluationId) {
        try {
            dao.remove(evaluationId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<EvaluationDetailDto> searchAll() {
        try {
            return dao.searchAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<EvaluationDetailDto> getStudentAllEvaluation(int studentId) {
        try {
            return dao.getStudentAllEvaluation(studentId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<EvaluationDetailDto> getInstructorAllEvaluation(int instructorId) {
        try {
            return dao.getInstructorAllEvaluation(instructorId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }
}
