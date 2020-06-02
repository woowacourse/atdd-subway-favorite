package wooteco.subway.web.exception;

public class InvalidAuthenticationException extends AuthenticationException{
    public InvalidAuthenticationException(String message) {
        super(message);
    }
}
