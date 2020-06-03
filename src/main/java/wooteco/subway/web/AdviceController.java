package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.service.favorite.dto.ErrorResponse;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;
import wooteco.subway.web.member.exception.InvalidPasswordException;
import wooteco.subway.web.member.exception.NotExistDataException;

import java.nio.file.AccessDeniedException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class AdviceController {
    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     *
     * 찾는 데이터가 존재하지 않을 때, 비밀번호가 틀렸을 때
     */
    @ExceptionHandler({NotExistDataException.class, InvalidPasswordException.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final ErrorResponse response = new ErrorResponse(BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(BAD_REQUEST)
                .body(response);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        final ErrorResponse response = new ErrorResponse(METHOD_NOT_ALLOWED, e.getMessage());
        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(response);
    }

    /**
     * Authentication가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        final ErrorResponse response = new ErrorResponse(FORBIDDEN, e.getMessage());
        return ResponseEntity.status(FORBIDDEN)
                .body(response);
    }

    /**
     * 잘못된 Authorization 헤더로 요청할 경우
     */
    @ExceptionHandler(InvalidAuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidAuthenticationException(InvalidAuthenticationException e) {
        final ErrorResponse response = new ErrorResponse(UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        final ErrorResponse response = new ErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
