package model.dao;

import java.sql.SQLException;
import java.util.List;

import model.dto.BehaviorDto;

public interface BehaviorDao {
    void add(BehaviorDto behaviorDto) throws SQLException;
    void update(BehaviorDto behaviorDto) throws SQLException;
    void remove(int id) throws SQLException;
    List<BehaviorDto> searchSimilarByName(String name) throws SQLException;
    List<BehaviorDto> searchSimilarByName(String name, String sortColumn, String sortDirection) throws SQLException;
    BehaviorDto searchOneByName(String name) throws SQLException;
    List<BehaviorDto> searchByScore(int minScore, int maxScore) throws SQLException;
    List<BehaviorDto> searchAll() throws SQLException;
    List<BehaviorDto> searchAll(String sortColumn, String sortDirection) throws SQLException;

}
