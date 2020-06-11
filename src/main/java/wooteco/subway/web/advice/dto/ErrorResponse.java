package wooteco.subway.web.advice.dto;

public class ErrorResponse {
	private String message;

	ErrorResponse(String message) {
		this.message = message;
	}

	public static ErrorResponse of(Exception e) {
		return new ErrorResponse(e.getMessage());
	}

	public String getMessage() {
		return message;
	}
}
