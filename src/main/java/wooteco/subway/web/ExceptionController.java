package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.domain.member.MemberConstructException;
import wooteco.subway.service.member.CreateMemberException;
import wooteco.subway.service.member.dto.MemberErrorResponse;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = MemberConstructException.class)
    public ResponseEntity<MemberErrorResponse> handleException(MemberConstructException e) {
        return ResponseEntity.badRequest().body(MemberErrorResponse.of(e));
    }

    @ExceptionHandler(value = CreateMemberException.class)
    public ResponseEntity<MemberErrorResponse> handleException(CreateMemberException e) {
        return ResponseEntity.badRequest().body(MemberErrorResponse.of(e));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<UnknownErrorResponse> handleException(Exception e) {
        return ResponseEntity.badRequest().body(UnknownErrorResponse.of(e));
    }
}
