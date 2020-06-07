package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(Exception e) {
        return ResponseEntity
            .badRequest()
            .body(e.getMessage());
    }
}
