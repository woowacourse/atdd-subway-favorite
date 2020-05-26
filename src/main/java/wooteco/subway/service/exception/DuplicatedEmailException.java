package wooteco.subway.service.exception;

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(final String message) {
        super(message + " : 이메일이 중복되었습니다.");
    }
}
