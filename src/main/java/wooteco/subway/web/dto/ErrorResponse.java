package wooteco.subway.web.dto;

/**
 *    class description
 *
 *    @author HyungJu An, MinWoo Yim
 */
public class ErrorResponse {
	private final String errorMessage;

	ErrorResponse(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static ErrorResponse of(final String message) {
		return new ErrorResponse(message);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
