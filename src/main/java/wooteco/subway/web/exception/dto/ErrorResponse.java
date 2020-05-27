package wooteco.subway.web.exception.dto;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorResponse {
    private static final int HTTP_STATUS_BAD_REQUEST = 400;

    private int status;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(int status, FieldError fieldError) {
        this.status = status;
        this.message = fieldError.getDefaultMessage();
    }

    public static ErrorResponse of(String errorMessage) {
        return new ErrorResponse(HTTP_STATUS_BAD_REQUEST, errorMessage);
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(HTTP_STATUS_BAD_REQUEST, bindingResult.getFieldError());
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
