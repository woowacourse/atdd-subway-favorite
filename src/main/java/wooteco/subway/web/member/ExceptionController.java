package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.service.common.dto.ErrorResponse;
import wooteco.subway.service.common.dto.NotValidResponse;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<NotValidResponse> invalidHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(NotValidResponse.of(e));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> UnknownErrorHandler(RuntimeException e) {
        return ResponseEntity.status(500).body(ErrorResponse.of(e));
    }
}
