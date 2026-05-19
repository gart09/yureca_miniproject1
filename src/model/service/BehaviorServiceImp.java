package model.service;

import model.dao.BehaviorDao;
import model.dao.BehaviorDaoImp;
import model.dto.BehaviorDto;
import model.dto.CanNotFindException;
import model.dto.DuplicateBehaviorException;

import java.sql.SQLException;
import java.util.List;

public class BehaviorServiceImp implements  BehaviorService {
    private BehaviorDao dao = new BehaviorDaoImp();

    @Override
    public void add(BehaviorDto behaviorDto) {
        try {
            String name = behaviorDto.getName();
            BehaviorDto find = dao.searchOneByName(name);
            if (find != null) {
                throw new DuplicateBehaviorException(name);
            }
            dao.add(behaviorDto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void update(BehaviorDto behaviorDto) {
        try {
            BehaviorDto find = dao.searchOneByName(behaviorDto.getName());
            if(find != null && find.getBehaviorId() != behaviorDto.getBehaviorId())
                throw new DuplicateBehaviorException(behaviorDto.getName());
            dao.update(behaviorDto);
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
    public List<BehaviorDto> searchSimilarByName(String name) {
        try {
            return dao.searchSimilarByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<BehaviorDto> searchByScore(int minScore, int maxScore) {
        try {
            return dao.searchByScore(minScore, maxScore);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<BehaviorDto> searchAll() {
        try {
            return dao.searchAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<BehaviorDto> searchAll(String sortColumn, String sortDirection) {
        try {
            return dao.searchAll(sortColumn, sortDirection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }
}
