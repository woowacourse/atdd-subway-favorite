package wooteco.subway.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.exception.LoginFailException;
import wooteco.subway.service.exception.ErrorResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
		return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(LoginFailException.class)
	public ResponseEntity<ErrorResponse> handleLoginFail(LoginFailException e) {
		return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidAuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidAuthentication(
		InvalidAuthenticationException e) {
		return ResponseEntity.status(HttpStatus.FOUND)
			.header(HttpHeaders.LOCATION, "/login")
			.body(new ErrorResponse(e.getMessage()));
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException e) {
		return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleRuntime(Exception e) {
		return new ResponseEntity<>(new ErrorResponse("예기치 않은 오류가 발생하였습니다."),
			HttpStatus.BAD_REQUEST);
	}
}