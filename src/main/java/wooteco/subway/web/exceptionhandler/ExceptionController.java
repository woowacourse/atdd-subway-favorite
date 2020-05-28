package wooteco.subway.web.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;
import wooteco.subway.exception.NotFoundUserException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        ExceptionResponse exceptionResponse = new ExceptionResponse(bindingResult.getFieldError());

        return ResponseEntity.badRequest()
            .body(exceptionResponse);
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<ExceptionResponse> notFoundUserException(NotFoundUserException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionResponse> jwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<ExceptionResponse> illegalAccessError(IllegalAccessError e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ExceptionResponse(e.getMessage()));
    }
}
