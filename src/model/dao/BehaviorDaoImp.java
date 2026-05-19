package model.dao;

import model.dto.BehaviorDto;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BehaviorDaoImp implements BehaviorDao {
    private DBUtil dbutil = DBUtil.getInstance();

    @Override
    public void add(BehaviorDto behaviorDto) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        String sql = " INSERT INTO behavior (name, score) VALUES (?, ?)";
        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, behaviorDto.getName());
            stmt.setInt(idx++, behaviorDto.getScore());

            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public void update(BehaviorDto behaviorDto) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE behavior SET name = ?, score = ? WHERE behaviorId = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            int idx = 1;

            stmt.setString(idx++, behaviorDto.getName());
            stmt.setInt(idx++, behaviorDto.getScore());
            stmt.setInt(idx++, behaviorDto.getBehaviorId());

            stmt.executeUpdate();

        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public void remove(BehaviorDto behaveDto) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql = "DELETE FROM behavior WHERE behaviorId = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, behaveDto.getBehaviorId());
            stmt.executeUpdate();

        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public List<BehaviorDto> searchSimilarByName(String name) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BehaviorDto> result = new ArrayList<>();
        String sql = " SELECT * FROM behavior WHERE name LIKE ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + name + "%");

            rs = stmt.executeQuery();
            while(rs.next()){
                BehaviorDto behaviorDto = new BehaviorDto();
                behaviorDto.setBehaviorId(rs.getInt("behavior_id"));
                behaviorDto.setName(rs.getString("name"));
                behaviorDto.setScore(rs.getInt("score"));

                result.add(behaviorDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }

        return result;
    }

    @Override
    public BehaviorDto searchOneByName(String name) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BehaviorDto behaviorDto = new BehaviorDto();
        String sql = "SELECT * FROM behavior WHERE name = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if(rs.next()){
                behaviorDto.setBehaviorId(rs.getInt("behavior_id"));
                behaviorDto.setName(rs.getString("name"));
                behaviorDto.setScore(rs.getInt("score"));
            } else {
                throw new SQLException("검색 결과가 없습니다.");
            }
        } finally {
            dbutil.close(stmt, con);
        }
        return behaviorDto;
    }

    @Override
    public List<BehaviorDto> searchByScore(int minScore, int maxScore) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BehaviorDto> result = new ArrayList<>();
        String sql = " SELECT * FROM behavior WHERE score BETWEEN ? and ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, minScore);
            stmt.setInt(2, maxScore);

            rs = stmt.executeQuery();
            while(rs.next()){
                BehaviorDto behaviorDto = new BehaviorDto();
                behaviorDto.setBehaviorId(rs.getInt("behavior_id"));
                behaviorDto.setName(rs.getString("name"));
                behaviorDto.setScore(rs.getInt("score"));

                result.add(behaviorDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }

        return result;
    }

    @Override
    public List<BehaviorDto> searchAll() throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BehaviorDto> result = new ArrayList<>();
        String sql = " SELECT * FROM behavior";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);

            rs = stmt.executeQuery();
            while(rs.next()){
                BehaviorDto behaviorDto = new BehaviorDto();
                behaviorDto.setBehaviorId(rs.getInt("behavior_id"));
                behaviorDto.setName(rs.getString("name"));
                behaviorDto.setScore(rs.getInt("score"));

                result.add(behaviorDto);
            }
        } finally {
            dbutil.close(stmt, con);
        }

        return result;
    }
}
