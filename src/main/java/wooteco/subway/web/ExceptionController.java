package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.*;
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
		ErrorCode errorCode = ErrorCode.BAD_REQUEST;

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(errorCode.getStatus(), errorCode.getErrorMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException() {
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorResponse.of(errorCode.getStatus(), errorCode.getErrorMessage()));
	}

	@ExceptionHandler(InvalidAuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidAuthenticationException() {
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(ErrorResponse.of(errorCode.getStatus(), errorCode.getErrorMessage()));
	}

	@ExceptionHandler(SameSourceTargetException.class)
	public ResponseEntity<ErrorResponse> handleSameSourceTargetException() {
		ErrorCode errorCode = ErrorCode.SAME_SOURCE_TARGET;

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(errorCode.getStatus(), errorCode.getErrorMessage()));
	}

	@ExceptionHandler(StationNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleStationNotFoundException(StationNotFoundException exception) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(exception.getStatus(), exception.getErrorMessage()));
	}

	@ExceptionHandler(LineNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleLineNotFoundException(LineNotFoundException exception) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(exception.getStatus(), exception.getErrorMessage()));
	}

	@ExceptionHandler(LineStationNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleLineStationNotFoundException(LineStationNotFoundException exception) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(exception.getStatus(), exception.getErrorMessage()));
	}
}
