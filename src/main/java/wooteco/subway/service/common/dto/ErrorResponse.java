package wooteco.subway.service.common.dto;

import java.util.Objects;

public class ErrorResponse {
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse of(Exception e) {
        String message = e.getMessage();
        if (Objects.isNull(message)) {
            return new ErrorResponse("DEFAULT_ERROR");
        }
        return new ErrorResponse(message);
    }

    public String getMessage() {
        return message;
    }
}