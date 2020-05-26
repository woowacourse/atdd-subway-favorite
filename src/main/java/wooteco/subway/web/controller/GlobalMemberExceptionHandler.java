package wooteco.subway.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.web.exception.ErrorResponse;

@RestControllerAdvice
public class GlobalMemberExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> exceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessages.get(0)));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> temporalExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
