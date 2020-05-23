package wooteco.subway.web;

import ch.qos.logback.access.pattern.StatusCodeConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.service.member.dto.ErrorResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> InvalidAuthenticationExceptionHandler(InvalidAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("비밀번호를 잘못 입력하셨습니다."));
        //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
