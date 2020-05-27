package wooteco.subway.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.web.exception.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> notExpectedException() {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of("개발자도 예상하지 못한 문제입니다!"));
    }

    @ExceptionHandler(SameStationException.class)
    public ResponseEntity<ErrorResponse> sameStationException(SameStationException e) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(e.getBindingResult()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalInputRequestException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> notFoundIdException() {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of("해당 값을 찾지 못했습니다!"));
    }
}
