package wooteco.subway.exception;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DbActionExecutionException.class)
    public ResponseEntity dbActionExecutionException(DbActionExecutionException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity duplicatedEmailException(DuplicateEmailException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoSuchMemberException.class)
    public ResponseEntity noSuchMemberException(NoSuchMemberException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
