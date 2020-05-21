package wooteco.subway.web.member;

public class NotExistEmailException extends RuntimeException {
    public NotExistEmailException() {
        super("email이 존재하지 않습니다.");
    }
}
