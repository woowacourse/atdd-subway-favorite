package wooteco.subway.web.exception;

public class UnauthorizedException extends SubwayException {
    public static final String REQUIRE_LOGIN_MESSAGE = "로그인을 해주세요.";

    public UnauthorizedException(String message) {
        super(message);
    }
}
