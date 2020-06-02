package wooteco.subway.exception;

public class ExceptionResponse {
    private final String message;

    private ExceptionResponse(Exception e) {
        this.message = e.getMessage();
    }

    public static ExceptionResponse of(Exception e) {
        return new ExceptionResponse(e);
    }

    public String getMessage() {
        return message;
    }
}
