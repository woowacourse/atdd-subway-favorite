package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

/**
 * class description
 *
 * @author ParkDooWon
 */
public enum ErrorCode implements ErrorStatus {
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYSTEM_ERROR"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
	SAME_SOURCE_TARGET(HttpStatus.BAD_REQUEST, "출발역과 도착역이 같을 수 없습니다.");

	private final HttpStatus status;
	private final String errorMessage;

	ErrorCode(HttpStatus status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
	}

	@Override
	public HttpStatus getStatus() {
		return this.status;
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
