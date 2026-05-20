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
        String sql = "UPDATE behavior SET name = ?, score = ? WHERE behavior_id = ?";

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
    public void remove(int id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql = "DELETE FROM behavior WHERE behavior_id = ?";

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
    public BehaviorDto searchById(int id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BehaviorDto result = new BehaviorDto();
        String sql = "SELECT * FROM behavior WHERE behavior_id = ?";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            result.setBehaviorId(rs.getInt("behavior_id"));
            result.setName(rs.getString("name"));
            result.setScore(rs.getInt("score"));
        } finally {
            dbutil.close(stmt, con);
        }
        return result;
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
    public List<BehaviorDto> searchSimilarByName(String name, String sortColumn, String sortDirection) 
    		throws SQLException {
    	Connection con = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	List<BehaviorDto> result = new ArrayList<>();
    	
        String safeColumn = "name";
        if ("name".equals(sortColumn) || "score".equals(sortColumn)) {
            safeColumn = sortColumn;
        }
        String safeDirection = "DESC".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";

        // 동적 쿼리
        String sql = "SELECT * FROM behavior WHERE name LIKE ? ORDER BY "
        + safeColumn + " " + safeDirection + ", behavior_id ASC";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + name + "%"); // 검색어 바인딩

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
            if(rs.next()) {
                behaviorDto.setBehaviorId(rs.getInt("behavior_id"));
                behaviorDto.setName(rs.getString("name"));
                behaviorDto.setScore(rs.getInt("score"));
            } else {
                return null;
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
            while (rs.next()) {
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
    public List<BehaviorDto> searchAll(String sortColumn, String sortDirection) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BehaviorDto> result = new ArrayList<>();

        // SQL 인젝션 방어: 사용자가 정렬할 수 있는 컬럼만 허용합니다.
        String safeColumn = "name";
        if ("name".equals(sortColumn) || "score".equals(sortColumn)) {
            safeColumn = sortColumn;
        }

        // 방향도 ASC, DESC 둘 중 하나만 허용합니다.
        String safeDirection = "DESC".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";

        String sql = "SELECT * FROM behavior ORDER BY " + safeColumn + " " + safeDirection
                + ", behavior_id ASC";

        try {
            con = dbutil.getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
            	BehaviorDto dto = new BehaviorDto();
                dto.setBehaviorId(rs.getInt("behavior_id"));
                dto.setName(rs.getString("name"));
                dto.setScore(rs.getInt("score"));
                result.add(dto);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }

        return result;
    }
}
