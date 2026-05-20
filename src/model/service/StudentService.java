package model.service;

import model.dto.StudentDto;

import java.sql.SQLException;
import java.util.List;

public interface StudentService {
    void add(StudentDto studentDto);
    void update(StudentDto studentDto);
    void remove(int id);
    List<StudentDto> searchSimilarByName(String name);
    List<StudentDto> searchSimilarByName(String name, String sortColumn, String sortDirection);
    List<StudentDto> searchByScore(int minScore, int maxScore);
    List<StudentDto> searchTopPercent(int persent);
    List<StudentDto> searchRecentRewardedStudents(); // 상점
    List<StudentDto> searchRecentPenalizedStudents(); // 벌점
    List<StudentDto> searchAll();
    List<StudentDto> searchAll(String sortColumn, String sortDirection);
}
