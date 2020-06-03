package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public interface ErrorStatus {
    HttpStatus getStatus();

    String getErrorMessage();
}
