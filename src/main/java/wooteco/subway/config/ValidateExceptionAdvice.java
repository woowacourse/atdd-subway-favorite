package wooteco.subway.config;

import static java.util.stream.Collectors.*;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.service.member.InvalidMemberException;
import wooteco.subway.web.InvalidAuthenticationException;

@RestControllerAdvice
public class ValidateExceptionAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		final String errorMessage = e.getBindingResult()
		                             .getAllErrors()
		                             .stream()
		                             .map(DefaultMessageSourceResolvable::getDefaultMessage)
		                             .collect(joining("\n"));

		return ResponseEntity
			.badRequest()
			.body(errorMessage);
	}

	@ExceptionHandler(InvalidAuthenticationException.class)
	public ResponseEntity<String> handleInvalidAuthenticationException(InvalidAuthenticationException e) {
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(e.getMessage());
	}

	@ExceptionHandler({InvalidMemberException.class, DbActionExecutionException.class})
	public ResponseEntity<String> handleInvalidMemberOrDbActionExecutionException(RuntimeException e) {
		return ResponseEntity
			.badRequest()
			.body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleUnexpectedException(Exception e) {
		System.out.println(e.getMessage());

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(e.getMessage());
	}

}
