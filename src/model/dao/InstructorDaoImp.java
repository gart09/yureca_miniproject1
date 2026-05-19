package model.dao;

import model.dto.InstructorDto;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstructorDaoImp implements InstructorDao {
    private DBUtil dbutil = DBUtil.getInstance();

    @Override
    public void add(InstructorDto InstructorDto) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        String sql = " INSERT INTO instructor (name, age) VALUES (?, ?)";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, InstructorDto.getName());
            stmt.setInt(idx++, InstructorDto.getAge());

            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public void update(InstructorDto InstructorDto) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE instructor SET name = ?, age = ? WHERE instructor_id = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            int idx = 1;

            stmt.setString(idx++, InstructorDto.getName());
            stmt.setInt(idx++, InstructorDto.getAge());
            stmt.setInt(idx++, InstructorDto.getInstructorId());

            stmt.executeUpdate();

        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public void remove(int id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql = "DELETE FROM instructor WHERE instructor_id = ?";

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
    public List<InstructorDto> searchSimilarByName(String name) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<InstructorDto> result = new ArrayList<>();
        String sql = " SELECT * FROM instructor WHERE name = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, name);

            rs = stmt.executeQuery();
            while(rs.next()){
                InstructorDto InstructorDto = new InstructorDto();
                InstructorDto.setInstructorId(rs.getInt("instructor_id"));
                InstructorDto.setName(rs.getString("name"));
                InstructorDto.setAge(rs.getInt("age"));

                result.add(InstructorDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }

        return result;
    }

    @Override
    public List<InstructorDto> searchAll() throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<InstructorDto> result = new ArrayList<>();
        String sql = " SELECT * FROM instructor";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);

            rs = stmt.executeQuery();
            while(rs.next()){
                InstructorDto InstructorDto = new InstructorDto();
                InstructorDto.setInstructorId(rs.getInt("instructor_id"));
                InstructorDto.setName(rs.getString("name"));
                InstructorDto.setAge(rs.getInt("age"));

                result.add(InstructorDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }

        return result;
    }
}
