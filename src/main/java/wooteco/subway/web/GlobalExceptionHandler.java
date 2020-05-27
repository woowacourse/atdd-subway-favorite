package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.service.member.exception.DuplicateMemberException;
import wooteco.subway.service.member.exception.IncorrectPasswordException;
import wooteco.subway.service.member.exception.NotFoundMemberException;
import wooteco.subway.web.dto.ErrorCode;
import wooteco.subway.web.dto.ErrorResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT, e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMemberException() {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DUPLICATE_MEMBER_EMAIL);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectPasswordException() {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INCORRECT_PASSWORD);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundMemberException() {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_FOUND_MEMBER);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthenticationException() {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_AUTH_TOKEN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
     public ResponseEntity<ErrorResponse> handleException() {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(ErrorResponse.of(ErrorCode.SERVER_ERROR));
     }
}
