package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.service.exception.DuplicatedEmailException;
import wooteco.subway.web.dto.DefaultResponse;
import wooteco.subway.web.dto.ErrorCode;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<DefaultResponse<Void>> handleDuplicatedEmailException(DuplicatedEmailException e) {
        return ResponseEntity.badRequest().body(DefaultResponse.error(ErrorCode.MEMBER_DUPLICATED_EMAIL));
    }
}
