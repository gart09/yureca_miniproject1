package model.dao;

import model.dto.BehaviorDto;
import model.dto.InstructorDto;

import java.sql.SQLException;
import java.util.List;

public interface InstructorDao {
    void add(InstructorDto instructorDto) throws SQLException;
    void update(InstructorDto instructorDto) throws SQLException;
    void remove(int id) throws SQLException;
    List<InstructorDto> searchSimilarByName(String name) throws SQLException;
    List<InstructorDto> searchSimilarByName(String name, String sortColumn, String sortDirection) throws SQLException;
    List<InstructorDto> searchAll() throws SQLException;
    List<InstructorDto> searchAll(String sortColumn, String sortDirection) throws SQLException;
}
