package wooteco.subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.service.favorite.ExistedFavoriteException;
import wooteco.subway.service.member.ExistedEmailException;
import wooteco.subway.service.member.NotExistedEmailException;
import wooteco.subway.service.member.WrongPasswordException;
import wooteco.subway.service.path.DuplicatedStationException;
import wooteco.subway.service.path.NotExistedPathException;
import wooteco.subway.service.path.NotExistedStationException;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicatedStationException.class)
    public ExceptionResponse handleDuplicatedStation() {
        return new ExceptionResponse("DUPLICATED_STATION");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistedStationException.class)
    public ExceptionResponse handleNotExistedStation() {
        return new ExceptionResponse("NOT_EXISTED_STATION");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistedPathException.class)
    public ExceptionResponse handleNotExistedPath() {
        return new ExceptionResponse("NOT_EXISTED_PATH");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistedEmailException.class)
    public ExceptionResponse handleExistedEmail() {
        return new ExceptionResponse("EXISTED_EMAIL");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistedEmailException.class)
    public ExceptionResponse handleNotExistedEmail() {
        return new ExceptionResponse("NOT_EXISTED_EMAIL");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongPasswordException.class)
    public ExceptionResponse handleWrongPassword() {
        return new ExceptionResponse("WRONG_PASSWORD");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAuthenticationException.class)
    public ExceptionResponse handleInvalidAuthentication() {
        return new ExceptionResponse("INVALID_AUTHENTICATION");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistedFavoriteException.class)
    public ExceptionResponse handleExistedFavorite() {
        return new ExceptionResponse("EXISTED_FAVORITE");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleGeneralServerError() {
        return new ExceptionResponse("SERVER_ERROR");
    }
}
