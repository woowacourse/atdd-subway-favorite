package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.service.member.MemberException;
import wooteco.subway.web.dto.ErrorResponse;

/**
 *    예외처리를 담당하는 컨트롤러 클래스입니다.
 *
 *    @author HyungJu An, MinWoo Yim
 */
@ControllerAdvice
public class ExceptionAdvice {
	@ExceptionHandler(MemberException.class)
	public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {
		return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handlerMethodException(MethodArgumentNotValidException e) {
		return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(e.getMessage()));
	}
}
