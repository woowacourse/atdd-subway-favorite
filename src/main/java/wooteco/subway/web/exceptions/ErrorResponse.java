package wooteco.subway.web.exceptions;

public class ErrorResponse {
    private final int statusCode;
    private String errorMessage;

    public ErrorResponse(int statusCode, Exception exception) {
        this.statusCode = statusCode;
        this.errorMessage = exception.getMessage();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
