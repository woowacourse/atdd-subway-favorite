package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.service.station.NoExistStationException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NoExistStationException.class)
    public ResponseEntity<String> NoExistStationExceptionHandler(NoExistStationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<String> InvalidAuthenticationExceptionHandler(InvalidAuthenticationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidRegisterException.class)
    public ResponseEntity<String> InvalidRegisterExceptionHandler(InvalidRegisterException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidUpdateException.class)
    public ResponseEntity<String> InvalidUpdateExceptionHandler(InvalidUpdateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoExistMemberException.class)
    public ResponseEntity<String> NoSuchMemberExceptionHandler(NoExistMemberException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
