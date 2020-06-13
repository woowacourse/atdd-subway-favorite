package wooteco.subway.service.member.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("중복된 이메일을 입력하였습니다.");
    }
}
