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
    public ResponseEntity<ErrorResponse> getDuplicateKeyException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> getInternalServerException(Exception exception) {
        System.out.println(exception.getMessage());
        exception.getStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
