package wooteco.subway.service.member.dto;

public class ErrorResponse {
    private String errorMessage;

    private ErrorResponse() {
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
