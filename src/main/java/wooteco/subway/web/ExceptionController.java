package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exceptions.DuplicatedEmailException;
import wooteco.subway.exceptions.PasswordConflictException;
import wooteco.subway.web.dto.ExceptionResponse;

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

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PasswordConflictException.class)
	public ExceptionResponse methodArgumentNotValid(PasswordConflictException e) {
		return new ExceptionResponse(e.getMessage());
	}
}
