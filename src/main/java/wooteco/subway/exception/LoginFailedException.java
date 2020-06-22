package wooteco.subway.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String email) {
        super("로그인에 실패했습니다. 이메일 주소: " + email);
    }
}
