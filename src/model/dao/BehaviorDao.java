package model.dao;

import java.sql.SQLException;
import java.util.List;

import model.dto.BehaviorDto;

public interface BehaviorDao {
    void add(BehaviorDto behaveDto) throws SQLException;
    void update(BehaviorDto behaveDto) throws SQLException;
    void remove(BehaviorDto behaveDto) throws SQLException;
    List<BehaviorDto> searchSimilarByName(String name) throws SQLException;
    BehaviorDto searchOneByName(String name) throws SQLException;
    List<BehaviorDto> searchByScore(int minScore, int maxScore) throws SQLException;
    List<BehaviorDto> searchAll() throws SQLException;
}
