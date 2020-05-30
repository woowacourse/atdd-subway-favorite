package wooteco.subway.web;

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
    @ExceptionHandler(value = {DuplicateEmailException.class, InvalidMemberIdException.class,
            InvalidMemberEmailException.class, DuplicateFavoriteException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> getDefined(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> getException(Exception exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse());
    }
}
