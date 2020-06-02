package wooteco.subway.exception.authentication;

public class InvalidSessionException extends InvalidAuthenticationException{
    public InvalidSessionException() {
        super("세션이 이상해요");
    }
}
