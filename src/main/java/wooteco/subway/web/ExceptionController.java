package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exceptions.*;
import wooteco.subway.web.dto.ExceptionResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class ExceptionController {

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({DuplicatedEmailException.class, DuplicatedFavoritePathException.class})
	public ExceptionResponse duplicated(RuntimeException e) {
		return new ExceptionResponse(e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({NotExistStationException.class, NotExistFavoritePathException.class,
			NotExistFavoritePathsException.class})
	public ExceptionResponse notExisted(RuntimeException e) {
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
	public ExceptionResponse invalidLogin(RuntimeException e) {
		return new ExceptionResponse(e.getMessage());
	}
}
