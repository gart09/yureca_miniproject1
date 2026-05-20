package model.service;

import model.dao.EvaluationDao;
import model.dao.EvaluationDaoImp;
import model.dao.InstructorDao;
import model.dao.InstructorDaoImp;
import model.dto.BehaviorDto;
import model.dto.EvaluationDetailDto;
import model.dto.InstructorDto;

import java.sql.SQLException;
import java.util.List;

public class InstructorServiceImp implements  InstructorService {
    private InstructorDao dao = new InstructorDaoImp();

    @Override
    public void add(InstructorDto instructorDto) {
        try {
            String name = instructorDto.getName();
            dao.add(instructorDto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void update(InstructorDto instructorDto) {
        try {
            dao.update(instructorDto);
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
    public InstructorDto searchById(int id) {
        try {
            return dao.searchById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<InstructorDto> searchSimilarByName(String name) {
        try {
            return dao.searchSimilarByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<InstructorDto> searchSimilarByName(String name, String sortColumn, String sortDirection) {
        try {
            return dao.searchSimilarByName(name, sortColumn, sortDirection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<InstructorDto> searchAll() {
        try {
            return dao.searchAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<InstructorDto> searchAll(String sortColumn, String sortDirection) {
        try {
            return dao.searchAll(sortColumn, sortDirection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }
}
