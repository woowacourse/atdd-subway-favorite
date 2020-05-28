package wooteco.subway.common.advice;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.web.dto.DefaultResponse;
import wooteco.subway.web.dto.ErrorCode;

@RestControllerAdvice
@Order(2)
public class CommonControllerAdvice {
    private static Logger logger = LoggerFactory.getLogger(CommonControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse<Void>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {

        String messages = e.getBindingResult()
            .getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("\n"));

        logger.error(messages);

        return ResponseEntity.badRequest()
            .body(DefaultResponse.message(messages));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponse<Void>> handleException(Exception e) {
        logger.error(e.getMessage());

        return new ResponseEntity<>(DefaultResponse.error(ErrorCode.SYSTEM_ERROR),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
