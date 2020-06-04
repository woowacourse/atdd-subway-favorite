package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class LineNotFoundException extends RuntimeException implements ErrorStatus {
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private String errorMessage = "존재하지 않는 노선입니다.";

    public LineNotFoundException(String message) {
        super(message);
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
