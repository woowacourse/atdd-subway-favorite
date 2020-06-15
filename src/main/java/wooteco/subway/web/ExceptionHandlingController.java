package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.exception.LoginFailException;
import wooteco.subway.service.exception.dto.ErrorResponse;

@RestControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleLoginFailException(MethodArgumentNotValidException error) {
        System.err.println("MethodArgumentNotValidException: " + error.getMessage());
        return new ResponseEntity<>(ErrorResponse.of("올바른 입력이 아닙니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({LoginFailException.class})
    public ResponseEntity<ErrorResponse> handleLoginFailException(LoginFailException error) {
        System.err.println("LoginFailException: " + error.getMessage());
        return new ResponseEntity<>(ErrorResponse.of("로그인에 실패하였습니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException error) {
        System.err.println("EntityNotFoundException: " + error.getMessage());
        return new ResponseEntity<>(ErrorResponse.of("Entity를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception error) {
        System.err.println("Exception: " + error.getMessage());
        return new ResponseEntity<>(ErrorResponse.of("검증에 실패하였습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}