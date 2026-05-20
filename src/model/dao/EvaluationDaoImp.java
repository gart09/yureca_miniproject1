package model.dao;

import model.dto.EvaluationDetailDto;
import model.dto.EvaluationDto;
import model.dto.StudentDto;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EvaluationDaoImp implements EvaluationDao {
    private DBUtil dbutil = DBUtil.getInstance();

    @Override
    public void add(EvaluationDto evaluationDto) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        String sql = " INSERT INTO evaluation (student_id, instructor_id, behavior_id, student_score, evaluated_at) VALUES (?, ?, ?, ?, ?)";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);

            int idx = 1;

            stmt.setInt(idx++, evaluationDto.getStudentId());
            stmt.setInt(idx++, evaluationDto.getInstructorId());
            stmt.setInt(idx++, evaluationDto.getBehaviorId());
            stmt.setInt(idx++, evaluationDto.getStudentScore());
            stmt.setObject(idx++, evaluationDto.getEvaluatedAt());


            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public void remove(int id)  throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        String sql = " DELETE FROM evaluation where evaluation_id = ?";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public List<EvaluationDetailDto> searchAll() throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EvaluationDetailDto> result = new ArrayList<>();

        String sql =
                " SELECT " +
                "e.evaluation_id as evaluation_id, " +
                "i.name as instructor_name, " +
                "s.name as student_name, " +
                "e.student_score as student_score, " +
                "b.name as behavior_name, " +
                "b.score as behavior_score, " +
                "e.evaluated_at " +
                "FROM evaluation e " +
                "JOIN instructor i ON e.instructor_id = i.instructor_id " +
                "JOIN student s ON e.student_id = s.student_id " +
                "JOIN behavior b ON e.behavior_id = b.behavior_id";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                EvaluationDetailDto evaluationDetailDto = new EvaluationDetailDto();
                evaluationDetailDto.setEvaluationId(rs.getInt("evaluation_id"));
                evaluationDetailDto.setInstructorName(rs.getString("instructor_name"));
                evaluationDetailDto.setStudentName(rs.getString("student_name"));
                evaluationDetailDto.setStudentScore(rs.getInt("student_score"));
                evaluationDetailDto.setBehaviorName(rs.getString("behavior_name"));
                evaluationDetailDto.setBehaviorScore(rs.getInt("behavior_score"));
                evaluationDetailDto.setEvaluatedAt(rs.getObject("evaluated_at", LocalDateTime.class));

                result.add(evaluationDetailDto);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return result;
    }

    @Override
    public List<EvaluationDetailDto> getStudentAllEvaluation(int studentId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EvaluationDetailDto> result = new ArrayList<>();

        String sql =
                " SELECT " +
                "e.evaluation_id as evaluation_id, " +
                "i.name as instructor_name, " +
                "s.name as student_name, " +
                "e.student_score as student_score, " +
                "b.name as behavior_name, " +
                "b.score as behavior_score, " +
                "e.evaluated_at " +
                "FROM evaluation e " +
                "JOIN instructor i ON e.instructor_id = i.instructor_id " +
                "JOIN student s ON e.student_id = s.student_id " +
                "JOIN behavior b ON e.behavior_id = b.behavior_id " +
                "WHERE e.student_id = ?";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            while(rs.next()){
                EvaluationDetailDto evaluationDetailDto = new EvaluationDetailDto();
                evaluationDetailDto.setEvaluationId(rs.getInt("evaluation_id"));
                evaluationDetailDto.setInstructorName(rs.getString("instructor_name"));
                evaluationDetailDto.setStudentName(rs.getString("student_name"));
                evaluationDetailDto.setStudentScore(rs.getInt("student_score"));
                evaluationDetailDto.setBehaviorName(rs.getString("behavior_name"));
                evaluationDetailDto.setBehaviorScore(rs.getInt("behavior_score"));
                evaluationDetailDto.setEvaluatedAt(rs.getObject("evaluated_at", LocalDateTime.class));

                result.add(evaluationDetailDto);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return result;
    }

    @Override
    public List<EvaluationDetailDto> getInstructorAllEvaluation(int instructorId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EvaluationDetailDto> result = new ArrayList<>();

        String sql =
                " SELECT " +
                "e.evaluation_id as evaluation_id, " +
                "i.name as instructor_name, " +
                "s.name as student_name, " +
                "e.student_score as student_score, " +
                "b.name as behavior_name, " +
                "b.score as behavior_score, " +
                "e.evaluated_at " +
                "FROM evaluation e " +
                "JOIN instructor i ON e.instructor_id = i.instructor_id " +
                "JOIN student s ON e.student_id = s.student_id " +
                "JOIN behavior b ON e.behavior_id = b.behavior_id " +
                "WHERE e.instructor_id = ?";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, instructorId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                EvaluationDetailDto evaluationDetailDto = new EvaluationDetailDto();
                evaluationDetailDto.setEvaluationId(rs.getInt("evaluation_id"));
                evaluationDetailDto.setInstructorName(rs.getString("instructor_name"));
                evaluationDetailDto.setStudentName(rs.getString("student_name"));
                evaluationDetailDto.setStudentScore(rs.getInt("student_score"));
                evaluationDetailDto.setBehaviorName(rs.getString("behavior_name"));
                evaluationDetailDto.setBehaviorScore(rs.getInt("behavior_score"));
                evaluationDetailDto.setEvaluatedAt(rs.getObject("evaluated_at", LocalDateTime.class));

                result.add(evaluationDetailDto);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return result;
    }
}
