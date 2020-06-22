package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.exception.LoginFailedException;
import wooteco.subway.exception.SameSourceTargetException;
import wooteco.subway.service.member.dto.ErrorResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorResponse("INVALID_PARAM"));
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthenticationException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED.value())
                .body(new ErrorResponse("UNAUTHORIZED"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorResponse("NOT_EXISTENT_DATA"));
    }

    @ExceptionHandler(SameSourceTargetException.class)
    public ResponseEntity<ErrorResponse> handleSameSourceTargetException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorResponse("SAME_SOURCE_TARGET_STATION"));
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ErrorResponse> handleLoginFailed() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED.value())
                .body(new ErrorResponse("LOGIN_FAILED"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("SYSTEM_ERROR"));
    }
}
