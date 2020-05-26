package wooteco.subway.web.exceptions;

public class ErrorResponse {
    private int statusCode;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(final int statusCode, Exception exception) {
        this.statusCode = statusCode;
        this.message = exception.getMessage();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
