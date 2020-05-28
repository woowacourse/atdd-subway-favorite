package wooteco.subway.web;

public class UserUnauthorizedException extends BusinessException {
    public UserUnauthorizedException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
