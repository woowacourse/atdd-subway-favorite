package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoSuchFavoriteException extends RuntimeException {
    public static final String NO_SUCH_FAVORITE_EXCEPTION_MESSAGE = "해당하는 즐겨찾기를 찾을 수 없습니다.";

    public NoSuchFavoriteException() {
        super(NO_SUCH_FAVORITE_EXCEPTION_MESSAGE);
    }

}
