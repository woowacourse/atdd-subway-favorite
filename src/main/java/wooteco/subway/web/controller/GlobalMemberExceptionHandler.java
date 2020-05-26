package wooteco.subway.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.web.exception.AuthenticationException;
import wooteco.subway.web.exception.BusinessException;
import wooteco.subway.web.exception.ErrorResponse;

@RestControllerAdvice
public class GlobalMemberExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "입력 오류")
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleRequestException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessages.get(0)));
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "인증 오류")
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizeException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "비즈니스 오류")
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponse> handlerBusinessException(BusinessException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "알수없는 오류")
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
