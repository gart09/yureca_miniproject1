package model.service;

import model.dto.BehaviorDto;

import java.util.List;

public interface BehaviorService {
    void add(BehaviorDto behaviorDto);
    void update(BehaviorDto behaviorDto);
    void remove(BehaviorDto behaviorDto);
    BehaviorDto searchOneByName(String name);
    List<BehaviorDto> searchSimilarByName(String name);
    List<BehaviorDto> searchByScore(int minScore, int maxScore);
    List<BehaviorDto> searchAll();
}
