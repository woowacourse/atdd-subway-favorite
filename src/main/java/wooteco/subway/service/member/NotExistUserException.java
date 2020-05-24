package wooteco.subway.service.member;

public class NotExistUserException extends RuntimeException {
    public NotExistUserException() {
        super("회원정보가 없습니다.");
    }
}
