package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.web.member.InvalidAuthenticationException;

@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidAuthenticationException.class)
	public ResponseEntity<String> handleInvalidAuthenticationException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}
}
