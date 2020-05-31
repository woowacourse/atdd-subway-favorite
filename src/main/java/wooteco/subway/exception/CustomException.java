package wooteco.subway.exception;

import java.util.Objects;

public class CustomException extends RuntimeException {
    private final RuntimeException exception;
    private final String message;

    public CustomException(RuntimeException exception) {
        super(exception);
        this.exception = exception;
        this.message = exception.getMessage();
    }

    public CustomException(String message, RuntimeException exception) {
        super(getRuntimeMessage(message, exception));
        this.message = message;
        this.exception = exception;
    }

    private static String getRuntimeMessage(String message, RuntimeException exception) {
        if (Objects.isNull(exception.getMessage()) || exception.getMessage().isEmpty()) {
            return message;
        }
        return exception.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public RuntimeException getException() {
        return exception;
    }
}
