package wooteco.subway.web;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.SubwayException;

@RestControllerAdvice
public class Advice {
    public static final String BAD_REQUEST_MESSAGE = "잘못된 요청입니다.";
    public static final String DUPLICATE_MESSAGE = "중복된 이름을 허용하지 않습니다.";

    @ExceptionHandler(SubwayException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = BAD_REQUEST_MESSAGE)
    public void domainExceptionHandler(Exception e) {
    }

    @ExceptionHandler(DbActionExecutionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = DUPLICATE_MESSAGE)
    public void duplicateExceptionHandler(Exception e) {
    }
}
