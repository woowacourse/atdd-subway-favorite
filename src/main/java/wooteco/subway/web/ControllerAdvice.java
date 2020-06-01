package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sun.dc.path.PathException;
import wooteco.subway.web.exception.duplicated.DuplicatedException;
import wooteco.subway.web.exception.member.MemberException;
import wooteco.subway.web.exception.notfound.NotFoundException;

@RestControllerAdvice
public class ControllerAdvice {
	@ExceptionHandler(DuplicatedException.class)
	public ResponseEntity handleDuplicatedException(DuplicatedException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity handleNotFoundException(NotFoundException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(MemberException.class)
	public ResponseEntity handleMemberException(MemberException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(PathException.class)
	public ResponseEntity handlePathException(PathException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
