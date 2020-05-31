package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.LineNotFoundException;
import wooteco.subway.exception.LineStationNotFoundException;
import wooteco.subway.exception.SameSourceTargetException;
import wooteco.subway.exception.StationNotFoundException;
import wooteco.subway.service.member.dto.ErrorResponse;
import wooteco.subway.web.member.InvalidAuthenticationException;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException() {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("INVALID_PARAM"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException() {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse("SYSTEM_ERROR"));
	}

	@ExceptionHandler(InvalidAuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidAuthenticationException() {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse("UNAUTHORIZED"));
	}

	@ExceptionHandler(StationNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleStationNotFoundException() {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("존재하지 않는 역입니다."));
	}

	@ExceptionHandler(LineNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleLineNotFoundException() {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("존재하지 않는 노선입니다."));
	}

	@ExceptionHandler(LineStationNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleLineStationNotFoundException() {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("노선에 해당 역이 없습니다."));
	}

	@ExceptionHandler(SameSourceTargetException.class)
	public ResponseEntity<ErrorResponse> handleSameSourceTargetException() {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("SAME_SOURCE_TARGET_STATION"));
	}
}
