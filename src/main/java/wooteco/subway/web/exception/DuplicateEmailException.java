package wooteco.subway.web.exception;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException(String email) {
        super(email + " 이메일이 중복되었습니다.");
    }
}
