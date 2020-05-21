package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exceptions.DuplicatedEmailException;
import wooteco.subway.exceptions.InvalidEmailException;
import wooteco.subway.exceptions.InvalidPasswordException;
import wooteco.subway.web.dto.ExceptionResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class ExceptionController {

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DuplicatedEmailException.class)
	public ExceptionResponse duplicatedEmail(DuplicatedEmailException e) {
		return new ExceptionResponse(e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ExceptionResponse methodArgumentNotValid(MethodArgumentNotValidException e) {
		return new ExceptionResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({InvalidPasswordException.class, InvalidEmailException.class,
			InvalidAuthenticationException.class})
	public ExceptionResponse invalidPassword(RuntimeException e) {
		return new ExceptionResponse(e.getMessage());
	}
}
