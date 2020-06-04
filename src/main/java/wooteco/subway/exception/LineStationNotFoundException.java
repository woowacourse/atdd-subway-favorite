package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class LineStationNotFoundException extends RuntimeException implements ErrorStatus {
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private String errorMessage = "역에 해당 노선이 존재하지 않습니다.";

    public LineStationNotFoundException(String message) {
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
