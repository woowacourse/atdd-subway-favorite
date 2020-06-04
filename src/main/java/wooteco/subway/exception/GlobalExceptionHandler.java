package wooteco.subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String UNEXPECTED_EXCEPTION = "서버내부에 에러가 있는 것 같습니다!";
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DbActionExecutionException.class)
    public ResponseEntity<ExceptionResponse> dbActionExecutionException(DbActionExecutionException e) {
        logger.error("error >> {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("error >> {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ExceptionResponse> duplicatedEmailException(DuplicateEmailException e) {
        logger.error("error >> {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(NoSuchMemberException.class)
    public ResponseEntity<ExceptionResponse> noSuchMemberException(NoSuchMemberException e) {
        logger.error("error >> {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> runtimeException(RuntimeException e) {
        logger.error("error >> {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.of(UNEXPECTED_EXCEPTION));
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> invalidAuthenticationException(InvalidAuthenticationException e) {
        logger.error("error >> {}", e.getMessage(), e);
        return ResponseEntity.status(401).body(ExceptionResponse.of(e));
    }
}
