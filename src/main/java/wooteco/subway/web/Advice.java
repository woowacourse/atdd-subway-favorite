package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.NoSuchAccountException;
import wooteco.subway.exception.NoSuchStationException;
import wooteco.subway.exception.WrongPasswordException;

@RestControllerAdvice
public class Advice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({DuplicateEmailException.class, WrongPasswordException.class})
    public ResponseEntity<String> handleDuplicateEmail(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({NoSuchAccountException.class, NoSuchStationException.class})
    public ResponseEntity<String> handleNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
