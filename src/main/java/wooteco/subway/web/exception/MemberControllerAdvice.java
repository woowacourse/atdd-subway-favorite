package wooteco.subway.web.exception;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberControllerAdvice {
    @ExceptionHandler(MemberCreationException.class)
    public ResponseEntity<ExceptionResponse> getSqlException(MemberCreationException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> getException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(System.lineSeparator()));
        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }
}
