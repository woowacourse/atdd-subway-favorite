package wooteco.subway.exception;

public class UnauthenticatedException extends RuntimeException {
	public static final String INVALID_AUTHENTICATION = "유효하지 않은 인증입니다.";

	public UnauthenticatedException(String message) {
		super(message);
	}
}