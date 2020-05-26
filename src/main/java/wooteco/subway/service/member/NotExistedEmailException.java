package wooteco.subway.service.member;

public class NotExistedEmailException extends RuntimeException {
    public NotExistedEmailException() {
        super("존재하지 않는 이메일 입니다.");
    }
}
