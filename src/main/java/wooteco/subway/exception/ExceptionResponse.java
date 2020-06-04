package wooteco.subway.exception;

public class ExceptionResponse {
    private final String message;

    private ExceptionResponse(Exception e) {
        this.message = e.getMessage();
    }

    private ExceptionResponse(String message) {
        this.message = message;
    }

    public static ExceptionResponse of(Exception e) {
        return new ExceptionResponse(e);
    }

    public static ExceptionResponse of(String message) {
        return new ExceptionResponse(message);
    }

    public String getMessage() {
        return message;
    }
}
