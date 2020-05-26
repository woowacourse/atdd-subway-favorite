package wooteco.subway.web.exception;

public class DuplicatedEmailException extends BusinessException {
    public DuplicatedEmailException(String email) {
        super(email + " 이메일이 중복되었습니다.");
    }
}
