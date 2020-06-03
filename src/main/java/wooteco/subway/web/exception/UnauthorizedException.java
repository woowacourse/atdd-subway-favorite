package wooteco.subway.web.exception;

public class UnauthorizedException extends SubwayException {
    public static final String REQUIRE_LOGIN_MESSAGE = "로그인을 해주세요.";
    public static final String NO_ACCESS_TOKEN_MESSAGE = "권한이 없습니다.";

    public UnauthorizedException(String message) {
        super(message);
    }
}
