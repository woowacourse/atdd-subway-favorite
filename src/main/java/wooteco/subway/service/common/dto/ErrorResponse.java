package wooteco.subway.service.common.dto;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

public class ErrorResponse {
    private String message;

    private ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse of(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getFieldError().getDefaultMessage();
        if (Objects.isNull(message)) {
            return new ErrorResponse("DEFAULT_ERROR");
        }
        return new ErrorResponse(message);
    }

    public String getMessage() {
        return message;
    }
}