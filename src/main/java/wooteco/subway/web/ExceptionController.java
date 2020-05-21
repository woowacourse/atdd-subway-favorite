package wooteco.subway.web;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.service.member.dto.ErrorResponse;

@ControllerAdvice
public class ExceptionController {
    // TODO: 2020/05/20 커스텀 예외를 만들기
    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> getDuplicateKeyException(DuplicateKeyException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse("중복된 값을 입력하였습니다."));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> getRuntimeException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
}
