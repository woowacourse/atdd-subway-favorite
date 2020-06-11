package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.InvalidAuthenticationException;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity handleUnauthorized(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class,
        MethodArgumentNotValidException.class})
    public ResponseEntity handleBadRequest(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleRuntimeException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
