package wooteco.subway.web.advice.exception;

public class BadRequestForResourcesException extends BusinessException {
    public BadRequestForResourcesException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
