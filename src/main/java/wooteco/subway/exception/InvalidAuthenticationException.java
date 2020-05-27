package wooteco.subway.exception;

public class InvalidAuthenticationException extends RuntimeException{

    public InvalidAuthenticationException(String message) {
        super(message);
    }
}
