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
    public void add(BehaviorDto behaveDto) {
        try {
            String name = behaveDto.getName();
            BehaviorDto find = dao.searchOneByName(name);
            if (find != null) {
                throw new DuplicateBehaviorException(name);
            }
            dao.add(behaveDto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 등록 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public BehaviorDto searchOneByName(String name) {
        try {
            BehaviorDto find = dao.searchOneByName(name);
            if (find == null) {
                throw new CanNotFindException(name);
            }
            return find;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void update(BehaviorDto behaviorDto) {
        try {
            searchOneByName(behaviorDto.getName());
            dao.update(behaviorDto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 수정 중 시스템 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void remove(BehaviorDto behaviorDto) {
        try {
            searchOneByName(behaviorDto.getName());
            dao.remove(behaviorDto);
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
}