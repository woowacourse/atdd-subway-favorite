package wooteco.subway.service.common.dto;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

public class NotValidResponse {
    private String message;

    public NotValidResponse() {
    }

    public NotValidResponse(String message) {
        this.message = message;
    }

    public static NotValidResponse of(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getFieldError().getDefaultMessage();
        if (Objects.isNull(message)) {
            return new NotValidResponse("DEFAULT_ERROR");
        }
        return new NotValidResponse(message);
    }

    public String getMessage() {
        return message;
    }
}