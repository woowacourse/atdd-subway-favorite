package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exceptions.LoginFailException;
import wooteco.subway.service.common.dto.ErrorResponse;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<ErrorResponse> loginFailHandler(LoginFailException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e));
    }
}
