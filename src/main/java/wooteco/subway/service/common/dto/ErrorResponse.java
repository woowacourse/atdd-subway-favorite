package wooteco.subway.service.common.dto;

public class ErrorResponse {
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse of(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    public String getMessage() {
        return message;
    }
}
