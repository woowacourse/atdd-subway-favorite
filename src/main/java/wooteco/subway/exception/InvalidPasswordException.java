package wooteco.subway.exception;

import wooteco.subway.web.member.InvalidAuthenticationException;

public class InvalidPasswordException extends InvalidAuthenticationException {
	public InvalidPasswordException() {
		super("비밀번호가 유효하지 않아요.");
	}

}
