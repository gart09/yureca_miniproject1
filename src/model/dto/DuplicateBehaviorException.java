package model.dto;

public class DuplicateBehaviorException extends RuntimeException {
    public DuplicateBehaviorException(String name) {
        super(String.format("'%s'은 이미 등록된 행동입니다.", name));
    }
}
