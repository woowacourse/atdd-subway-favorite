package wooteco.subway.exception;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DbActionExecutionException.class)
    public ResponseEntity<ExceptionResponse> dbActionExecutionException(DbActionExecutionException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ExceptionResponse> duplicatedEmailException(DuplicateEmailException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(NoSuchMemberException.class)
    public ResponseEntity<ExceptionResponse> noSuchMemberException(NoSuchMemberException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> runtimeException(RuntimeException e) {
        return ResponseEntity.status(500).body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> invalidAuthenticationException(InvalidAuthenticationException e) {
        return ResponseEntity.status(401).body(ExceptionResponse.of(e));
    }
}
