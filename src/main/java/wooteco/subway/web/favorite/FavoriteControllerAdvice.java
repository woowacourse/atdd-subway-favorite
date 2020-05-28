package wooteco.subway.web.favorite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.DuplicateFavoriteException;
import wooteco.subway.exception.InvalidStationIdException;

@RestControllerAdvice(basePackages = "wooteco.subway.web.favorite")
public class FavoriteControllerAdvice {

    @ExceptionHandler({DuplicateFavoriteException.class, InvalidStationIdException.class})
    public ResponseEntity handleDuplicateFavoriteException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e);
    }
}
