package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.service.member.exception.MemberException;

@RestControllerAdvice
public class MemberControllerAdvice {
	@ExceptionHandler(MemberException.class)
	public ResponseEntity handleMemberException(MemberException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
