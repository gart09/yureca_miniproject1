package model.dao;

import model.dto.StudentDto;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImp implements StudentDao {
    private DBUtil dbutil = DBUtil.getInstance();

    @Override
    public void add(StudentDto StudentDto) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        String sql = " INSERT INTO student (name, age, score) VALUES (?, ?, ?)";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, StudentDto.getName());
            stmt.setInt(idx++, StudentDto.getAge());
            stmt.setInt(idx++, StudentDto.getScore());

            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public void update(StudentDto StudentDto) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE student SET name = ?, age = ?, score = ? WHERE student_id = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            int idx = 1;

            stmt.setString(idx++, StudentDto.getName());
            stmt.setInt(idx++, StudentDto.getScore());
            stmt.setInt(idx++, StudentDto.getAge());
            stmt.setInt(idx++, StudentDto.getStudentId());

            stmt.executeUpdate();

        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public void remove(int id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql = "DELETE FROM student WHERE student_id = ?";

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
    public List<StudentDto> searchSimilarByName(String name) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<StudentDto> result = new ArrayList<>();
        String sql = " SELECT * FROM student WHERE name = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, name);

            rs = stmt.executeQuery();
            while(rs.next()){
                StudentDto StudentDto = new StudentDto();
                StudentDto.setStudentId(rs.getInt("student_id"));
                StudentDto.setName(rs.getString("name"));
                StudentDto.setAge(rs.getInt("age"));
                StudentDto.setScore(rs.getInt("score"));

                result.add(StudentDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }

        return result;
    }

    @Override
    public List<StudentDto> searchByScore(int minScore, int maxScore) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<StudentDto> result = new ArrayList<>();
        String sql = " SELECT * FROM student WHERE score BETWEEN ? and ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, minScore);
            stmt.setInt(2, maxScore);

            rs = stmt.executeQuery();
            while(rs.next()){
                StudentDto StudentDto = new StudentDto();
                StudentDto.setStudentId(rs.getInt("student_id"));
                StudentDto.setName(rs.getString("name"));
                StudentDto.setAge(rs.getInt("age"));
                StudentDto.setScore(rs.getInt("score"));

                result.add(StudentDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }

        return result;
    }

    @Override
    public List<StudentDto> searchAll() throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<StudentDto> result = new ArrayList<>();
        String sql = " SELECT * FROM student";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);

            rs = stmt.executeQuery();
            while(rs.next()){
                StudentDto StudentDto = new StudentDto();
                StudentDto.setStudentId(rs.getInt("student_id"));
                StudentDto.setName(rs.getString("name"));
                StudentDto.setAge(rs.getInt("age"));
                StudentDto.setScore(rs.getInt("score"));

                result.add(StudentDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }

        return result;
    }
}
