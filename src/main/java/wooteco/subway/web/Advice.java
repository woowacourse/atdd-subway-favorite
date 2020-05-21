package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.SubwayException;

@RestControllerAdvice
public class Advice {
	@ExceptionHandler(SubwayException.class)
	public ResponseEntity exceptionHandler(Exception e) {
		return ResponseEntity
			.ok()
			.body(e.getMessage());
	}
}
