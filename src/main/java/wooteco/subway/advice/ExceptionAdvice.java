package wooteco.subway.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.InvalidPasswordException;
import wooteco.subway.exception.NotExistException;

@RestControllerAdvice
public class ExceptionAdvice {
    private Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        logger.error(String.format("error >> {%s}", e));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(String.format("error >> {%s}", e));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotExistException.class)
    public ResponseEntity handleNotExistException(NotExistException e) {
        logger.error(String.format("error >> {%s}", e));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity handleInvalidPasswordException(InvalidPasswordException e) {
        logger.error(String.format("error >> {%s}", e));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
