package wooteco.subway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.service.member.exception.DuplicateMemberException;
import wooteco.subway.service.member.exception.IncorrectPasswordException;
import wooteco.subway.service.member.exception.NotFoundMemberException;
import wooteco.subway.web.dto.ErrorResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse("유효한 입력이 아닙니다.", e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMemberException(
        DuplicateMemberException e) {
        logger.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse("이미 가입된 이메일 입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthenticationException(
        InvalidAuthenticationException e) {
        logger.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse("토큰이 유효하지 않습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectPasswordException(
        IncorrectPasswordException e) {
        logger.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse("비밀번호가 일치하지 않습니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundMemberException(NotFoundMemberException e) {
        logger.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse("사용자가 존재하지 않습니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("서버에서 오류가 발생했습니다."));
    }
}
