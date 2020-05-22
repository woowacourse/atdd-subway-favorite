package wooteco.subway.common.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.web.dto.DefaultResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String messages = e.getBindingResult()
                .getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        return ResponseEntity.badRequest().body(DefaultResponse.message(messages));
    }
}
