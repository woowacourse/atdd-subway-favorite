package wooteco.subway.web.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

public class ErrorResponse {
    private String code;
    private String message;
    private List<FieldError> errors;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String code, String message,
            BindingResult bindingResult) {
        this(code, message);
        this.errors = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> new FieldError(fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage())).collect(Collectors.toList());
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage(), bindingResult);
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }

    public String getCode() {
        return code;
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
