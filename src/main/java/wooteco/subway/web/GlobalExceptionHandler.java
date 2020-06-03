package wooteco.subway.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.AlreadyExistsEmailException;
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
	public ResponseEntity<ErrorResponse> handleUnauthorized(LoginFailException e) {
		return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidAuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleFound(
		InvalidAuthenticationException e) {
		return ResponseEntity.status(HttpStatus.FOUND)
			.header(HttpHeaders.LOCATION, "/login")
			.body(new ErrorResponse(e.getMessage()));
	}

	@ExceptionHandler(AlreadyExistsEmailException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
		return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();

		List<String> bodies = new ArrayList<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			bodies.add("[" + fieldError.getField() + "](은)는 " + fieldError.getDefaultMessage()
				+ " 입력된 값: [" + fieldError.getRejectedValue() + "]");
		}

		return new ResponseEntity<>(new ErrorResponse(String.join(", ", bodies)),
			HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleInternalServerError() {
		return new ResponseEntity<>(new ErrorResponse("예기치 않은 오류가 발생하였습니다."),
			HttpStatus.INTERNAL_SERVER_ERROR);
	}
}