package wooteco.subway.web.member;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.IllegalLoginRequestException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "wooteco.subway.web.member")
public class MemberControllerAdvice {

	@ExceptionHandler(IllegalLoginRequestException.class)
	public ResponseEntity handleIllegalLoginRequestException(IllegalLoginRequestException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
