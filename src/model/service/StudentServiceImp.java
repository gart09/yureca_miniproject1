package model.service;

import model.dao.StudentDao;
import model.dao.StudentDaoImp;
import model.dto.StudentDto;

import java.sql.SQLException;
import java.util.List;

public class StudentServiceImp implements  StudentService {
    private StudentDao dao = new StudentDaoImp();

    @Override
    public void add(StudentDto studentDto) {
        try {
            String name = studentDto.getName();
            dao.add(studentDto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void update(StudentDto studentDto) {
        try {
            dao.update(studentDto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 수정 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void remove(int id) {
        try {
            dao.remove(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 수정 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<StudentDto> searchSimilarByName(String name) {
        try {
            return dao.searchSimilarByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<StudentDto> searchSimilarByName(String name, String sortColumn, String sortDirection) {
        try {
            return dao.searchSimilarByName(name, sortColumn, sortDirection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<StudentDto> searchByScore(int minScore, int maxScore) {
        try {
            return dao.searchByScore(minScore, maxScore);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<StudentDto> searchAll() {
        try {
            return dao.searchAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<StudentDto> searchAll(String sortColumn, String sortDirection) {
        try {
            return dao.searchAll(sortColumn, sortDirection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }
}
