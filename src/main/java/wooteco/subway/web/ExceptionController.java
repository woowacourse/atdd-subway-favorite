package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.service.line.LineNotFoundException;
import wooteco.subway.service.member.DuplicatedEmailException;
import wooteco.subway.service.member.MemberNotFoundException;
import wooteco.subway.service.member.NonExistEmailException;
import wooteco.subway.service.member.WrongPasswordException;
import wooteco.subway.service.path.UnreachablePathException;
import wooteco.subway.service.station.StationNotFoundException;
import wooteco.subway.web.member.InvalidAuthenticationException;

@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler({LineNotFoundException.class, MemberNotFoundException.class, NonExistEmailException.class,
		WrongPasswordException.class, UnreachablePathException.class, StationNotFoundException.class,
		DuplicatedEmailException.class})
	public ResponseEntity<String> handleExceptions(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidAuthenticationException.class)
	public ResponseEntity<String> handleInvalidAuthenticationException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}
}
