package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
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
            InvalidMemberEmailException.class, DuplicateFavoriteException.class})
    public ResponseEntity<ErrorResponse> getDuplicateKeyException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
}
