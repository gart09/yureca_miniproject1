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

        String sql = " INSERT INTO evaluation (student_id, instructor_id, behavior_id, evaluated_at) VALUES (?, ?, ?, ?)";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setInt(idx++, evaluationDto.getStudentId());
            stmt.setInt(idx++, evaluationDto.getInstructorId());
            stmt.setInt(idx++, evaluationDto.getBehaviorId());
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
        List<EvaluationDetailDto> result = new ArrayList<>();

        String sql =" SELECT instructor_name, student_name, student_score, behavior_name, behavior_score, evaluated_at"
                +   "";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            ResultSet rs = null;
            rs = stmt.executeQuery();
            while(rs.next()){
                EvaluationDto EvaluationDto = new EvaluationDto();
                EvaluationDto.setEvaluationId(rs.getInt("evaluation_id"));
                EvaluationDto.setInstructorId(rs.getInt("instructor_id"));
                EvaluationDto.setStudentId(rs.getInt("student_id"));
                EvaluationDto.setBehaviorId(rs.getInt("behavior_id"));
                EvaluationDto.setEvaluatedAt(rs.getObject("evaluated_at", LocalDateTime.class));

                result.add(EvaluationDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }
        return result;
    }

    @Override
    public List<EvaluationDetailDto> getStudentAllEvaluation(int studentId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        List<EvaluationDetailDto> result = new ArrayList<>();

        String sql = " SELECT * FROM evaluation where student_id = ?";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, studentId);
            ResultSet rs = null;
            rs = stmt.executeQuery();
            while(rs.next()){
                EvaluationDto EvaluationDto = new EvaluationDto();
                EvaluationDto.setEvaluationId(rs.getInt("evaluation_id"));
                EvaluationDto.setInstructorId(rs.getInt("instructor_id"));
                EvaluationDto.setStudentId(rs.getInt("student_id"));
                EvaluationDto.setBehaviorId(rs.getInt("behavior_id"));
                EvaluationDto.setEvaluatedAt(rs.getObject("evaluated_at", LocalDateTime.class));

                result.add(EvaluationDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }
        return result;
    }

    @Override
    public List<EvaluationDetailDto> getInstructorAllEvaluation(int instructorId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        List<EvaluationDetailDto> result = new ArrayList<>();

        String sql = " SELECT * FROM evaluation where instructor_id = ?";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, instructorId);
            ResultSet rs = null;
            rs = stmt.executeQuery();
            while(rs.next()){
                EvaluationDto EvaluationDto = new EvaluationDto();
                EvaluationDto.setEvaluationId(rs.getInt("evaluation_id"));
                EvaluationDto.setInstructorId(rs.getInt("instructor_id"));
                EvaluationDto.setStudentId(rs.getInt("student_id"));
                EvaluationDto.setBehaviorId(rs.getInt("behavior_id"));
                EvaluationDto.setEvaluatedAt(rs.getObject("evaluated_at", LocalDateTime.class));

                result.add(EvaluationDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }
        return result;
    }
}
