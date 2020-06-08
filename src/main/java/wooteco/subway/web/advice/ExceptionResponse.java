package wooteco.subway.web.advice;

public class ExceptionResponse {
    private final String errorCode;
    private final String message;

    public ExceptionResponse(final String errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
