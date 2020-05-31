package wooteco.subway.exception;

public class ExceptionResponse {
    private String message;

    private ExceptionResponse() {
    }

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
