package wooteco.subway.exception.authentication;

public class InvalidTokenException extends InvalidAuthenticationException{
    public InvalidTokenException() {
        super("토큰이 이상해요");
    }
}
