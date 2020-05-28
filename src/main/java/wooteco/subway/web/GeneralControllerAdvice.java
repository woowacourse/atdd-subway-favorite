package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralControllerAdvice {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
