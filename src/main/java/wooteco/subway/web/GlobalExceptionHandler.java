package wooteco.subway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.exception.InvalidAuthenticationException;
import wooteco.subway.exception.LoginFailException;
import wooteco.subway.exception.SameSourceTargetStationException;
import wooteco.subway.service.exception.ErrorResponse;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String LOG_ERROR_MESSAGE_FORMAT = "ERROR MESSAGE >>> {}";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 내부에 예외가 발생했습니다.";
    private static final String ILLEGAL_REQUEST_ARGUMENT_MESSAGE = "요청중 올바르지 않는 인자가 포함되어있습니다.";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleInternalException(RuntimeException e) {
        LOGGER.error(LOG_ERROR_MESSAGE_FORMAT, e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleIllegalMethodArgument(MethodArgumentNotValidException e) {
        LOGGER.error(LOG_ERROR_MESSAGE_FORMAT, e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(ILLEGAL_REQUEST_ARGUMENT_MESSAGE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        LOGGER.error(LOG_ERROR_MESSAGE_FORMAT, e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<ErrorResponse> handleLoginFail(LoginFailException e) {
        LOGGER.error(LOG_ERROR_MESSAGE_FORMAT, e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthentication(InvalidAuthenticationException e) {
        LOGGER.error(LOG_ERROR_MESSAGE_FORMAT, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, "/login")
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({DuplicateEmailException.class, SameSourceTargetStationException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(RuntimeException e) {
        LOGGER.error(LOG_ERROR_MESSAGE_FORMAT, e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}