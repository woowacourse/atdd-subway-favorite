package wooteco.subway.web;

public class BadRequestForResourcesException extends BusinessException {
    public BadRequestForResourcesException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
