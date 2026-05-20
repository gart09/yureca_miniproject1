package model.dao;

import model.dto.BehaviorDto;
import model.dto.InstructorDto;
import model.dto.StudentDto;

import java.sql.SQLException;
import java.util.List;

public interface StudentDao {
    void add(StudentDto studentDto) throws SQLException;
    void update(StudentDto studentDto) throws SQLException;
    void remove(int id) throws SQLException;
    StudentDto searchById(int id) throws SQLException;
    List<StudentDto> searchSimilarByName(String name) throws SQLException;
    List<StudentDto> searchSimilarByName(String name, String sortColumn, String sortDirection) throws SQLException;
    List<StudentDto> searchByScore(int minScore, int maxScore) throws SQLException;
    List<StudentDto> searchTopPercent(int percent) throws SQLException;
    List<StudentDto> searchAll() throws SQLException;
    List<StudentDto> searchAll(String sortColumn, String sortDirection) throws SQLException;
}

  