package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAuthenticationException extends RuntimeException {

	public InvalidAuthenticationException(String message) {
		super(message);
	}

}
