package model.dao;

import model.dto.InstructorDto;
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
            stmt.setInt(idx++, StudentDto.getAge());
            stmt.setInt(idx++, StudentDto.getScore());
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
    public StudentDto searchById(int id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StudentDto result = new StudentDto();
        String sql = "SELECT * FROM student WHERE student_id = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            if(rs.next()) {
                result.setStudentId(rs.getInt("student_id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt("age"));
                result.setScore(rs.getInt("score"));
            }else{
                return null;
            }
        } finally {
            dbutil.close(stmt, con);
        }
        return result;
    }

    @Override
    public List<StudentDto> searchSimilarByName(String name) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<StudentDto> result = new ArrayList<>();
        String sql = " SELECT * FROM student WHERE name LIKE ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + name + "%");

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
    public List<StudentDto> searchSimilarByName(String name, String sortColumn, String sortDirection) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<StudentDto> result = new ArrayList<>();
        String safeColumn = getSortableColumn(sortColumn);
        String safeDirection = getSortDirection(sortDirection);
        String sql = "SELECT * FROM student WHERE name LIKE ? ORDER BY "
                + safeColumn + " " + safeDirection + ", student_id ASC";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + name + "%");

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

    @Override
    public List<StudentDto> searchAll(String sortColumn, String sortDirection) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<StudentDto> result = new ArrayList<>();
        String safeColumn = getSortableColumn(sortColumn);
        String safeDirection = getSortDirection(sortDirection);
        String sql = "SELECT * FROM student ORDER BY " + safeColumn + " " + safeDirection + ", student_id ASC";

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

    private String getSortableColumn(String sortColumn) {
        if ("name".equals(sortColumn) || "age".equals(sortColumn) || "score".equals(sortColumn)) {
            return sortColumn;
        }
        return "name";
    }

    private String getSortDirection(String sortDirection) {
        return "DESC".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";
    }

	@Override
	public List<StudentDto> searchTopPercent(int percent) throws SQLException {
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<StudentDto> result = new ArrayList<>();
        String subquery = "( SELECT student_id, name, age, score"
                + ", PERCENT_RANK() OVER (ORDER BY score DESC) as pct"
                + " FROM student )";
        String sql = "SELECT student_id, name, age, score"
                + " FROM " + subquery + " as ranked_student"
                + " WHERE pct <= (? / 100.0)";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, percent);

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
            dbutil.close(rs, stmt, con);
        }

        return result;
	}
	
	@Override
	public List<StudentDto> searchRecentHistory(boolean isReward) throws SQLException {
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<StudentDto> result = new ArrayList<>();
        
        // 상점: > , 벌점: <
        String operator = isReward ? ">" : "<";
        
        String sql = "SELECT s.student_id, s.name, s.age, s.score, "
                + "       b.name as behavior_name, h.score as behavior_score, h.date "
                + "FROM studentScoreHistory h "
                + "INNER JOIN student s ON h.student_id = s.student_id "
                + "INNER JOIN behavior b ON h.behavior_id = b.behavior_id "
                + "WHERE b.score " + operator + " 0 "
                + "ORDER BY h.date DESC, h.history_id DESC "
                + "LIMIT 1";
		
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
            dbutil.close(rs, stmt, con);
        }

        return result;
	}
	
	
}
