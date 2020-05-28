package wooteco.subway.service.member.dto;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
public class ErrorResponse {
	private String errorMessage;

	public ErrorResponse(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
