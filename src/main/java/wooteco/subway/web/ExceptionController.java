package wooteco.subway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.service.favorite.exception.DuplicateFavoriteException;
import wooteco.subway.service.member.dto.ErrorResponse;
import wooteco.subway.service.member.exception.DuplicateEmailException;
import wooteco.subway.service.member.exception.InvalidMemberEmailException;
import wooteco.subway.service.member.exception.InvalidMemberIdException;

@ControllerAdvice
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class.getSimpleName());

    @ExceptionHandler(value = {DuplicateEmailException.class, InvalidMemberIdException.class,
            InvalidMemberEmailException.class, DuplicateFavoriteException.class, MethodArgumentNotValidException.class, InvalidMemberEmailException.class})
    public ResponseEntity<ErrorResponse> getDefined(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> getException(Exception exception) {
        logger.error("Unexpected Error", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse());
    }
}
