package model.dto;

public class CanNotFindException extends RuntimeException {
    public CanNotFindException() {
    }

    public CanNotFindException(String name) {
        super(String.format("'%s'에 해당하는 행동 정보를 찾을 수 없습니다.", name));
    }
}
