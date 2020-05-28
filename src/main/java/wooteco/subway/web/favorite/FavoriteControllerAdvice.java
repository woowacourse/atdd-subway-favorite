package wooteco.subway.web.favorite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.service.favorite.exception.FavoriteException;

@RestControllerAdvice
public class FavoriteControllerAdvice {
	@ExceptionHandler(FavoriteException.class)
	public ResponseEntity handleFavoriteException(FavoriteException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
