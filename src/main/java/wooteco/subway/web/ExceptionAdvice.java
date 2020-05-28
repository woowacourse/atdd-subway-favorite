package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wooteco.subway.service.favorite.ExistedFavoriteException;
import wooteco.subway.service.member.ExistedEmailException;
import wooteco.subway.service.member.NotExistedEmailException;
import wooteco.subway.service.member.WrongPasswordException;
import wooteco.subway.service.path.DuplicatedStationException;
import wooteco.subway.service.path.NotExistedPathException;
import wooteco.subway.service.path.NotExistedStationException;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DuplicatedStationException.class, NotExistedPathException.class,
            ExistedEmailException.class, WrongPasswordException.class, ExistedFavoriteException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequest(BadRequestForResourcesException e) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(e.getErrorCode(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorized(UserUnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(e.getErrorCode(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NotExistedStationException.class, NotExistedEmailException.class})
    public ResponseEntity<ExceptionResponse> handleNotFound(ResourcesNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(e.getErrorCode(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralServerError(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse("SERVER_ERROR", e.getMessage()));
    }
}
