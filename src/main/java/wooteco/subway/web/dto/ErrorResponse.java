package wooteco.subway.web.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

public class ErrorResponse {
    private String message;
    private List<FieldError> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message,
            BindingResult bindingResult) {
        this.message = message;
        this.errors = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> new FieldError(fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage())).collect(Collectors.toList());
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    private class FieldError {
        private String field;
        private Object value;
        private String reason;

        public FieldError() {
        }

        public FieldError(String field, Object value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public String getField() {
            return field;
        }

        public Object getValue() {
            return value;
        }

        public String getReason() {
            return reason;
        }
    }
}
