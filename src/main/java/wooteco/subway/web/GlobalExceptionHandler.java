package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.service.member.exception.DuplicateMemberException;
import wooteco.subway.web.dto.ErrorResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse("유효한 입력이 아닙니다.", e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMemberException() {
        ErrorResponse errorResponse = new ErrorResponse("이미 가입된 이메일 입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthenticationException() {
        ErrorResponse errorResponse = new ErrorResponse("토큰이 유효하지 않습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("서버에서 오류가 발생했습니다."));
    }
}
