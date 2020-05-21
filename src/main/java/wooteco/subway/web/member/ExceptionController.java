package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(InvalidUpdateException.class)
    public ResponseEntity<String> InvalidUpdateExceptionHandler(InvalidUpdateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
