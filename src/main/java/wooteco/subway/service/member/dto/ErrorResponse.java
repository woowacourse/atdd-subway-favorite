package wooteco.subway.service.member.dto;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.ErrorStatus;

public class ErrorResponse implements ErrorStatus {
    private HttpStatus status;
    private String errorMessage;

    private ErrorResponse(HttpStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse of(HttpStatus status, String errorMessage) {
        return new ErrorResponse(status, errorMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
