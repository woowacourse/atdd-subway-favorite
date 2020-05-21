package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoginMemberControllerAdvice {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handleLoginException(RuntimeException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
