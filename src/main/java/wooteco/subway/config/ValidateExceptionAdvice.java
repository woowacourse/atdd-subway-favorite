package wooteco.subway.config;

import static java.util.stream.Collectors.*;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.service.member.InvalidMemberException;

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

	@ExceptionHandler(InvalidMemberException.class)
	public ResponseEntity<String> handleInvalidMemberException(InvalidMemberException e) {
		return ResponseEntity
			.badRequest()
			.body(e.getMessage());
	}

}
