package wooteco.subway.exception;

public class ExceptionResponse {
	private String message;

	public ExceptionResponse() {
	}

	public ExceptionResponse(String message) {
		this.message = message;
	}

	public static ExceptionResponse of(String message) {
		return new ExceptionResponse(message);
	}

	public String getMessage() {
		return message;
	}
}
