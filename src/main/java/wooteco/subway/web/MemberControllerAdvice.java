package wooteco.subway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.service.exception.DuplicatedEmailException;
import wooteco.subway.web.dto.DefaultResponse;
import wooteco.subway.web.dto.ErrorCode;
import wooteco.subway.web.member.auth.InvalidAuthenticationException;

@RestControllerAdvice
public class MemberControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(MemberControllerAdvice.class);

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<DefaultResponse<Void>> handleDuplicatedEmailException(DuplicatedEmailException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(DefaultResponse.error(ErrorCode.MEMBER_DUPLICATED_EMAIL));
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<DefaultResponse<Void>> handleInvalidAuthenticationException(InvalidAuthenticationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(DefaultResponse.error(e.getErrorCode()), HttpStatus.UNAUTHORIZED);
    }
}
