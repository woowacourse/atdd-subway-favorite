package wooteco.subway.web;

import ch.qos.logback.access.pattern.StatusCodeConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.service.member.dto.ErrorResponse;
import wooteco.subway.service.station.NoSuchStationException;
import wooteco.subway.web.member.DuplicateMemberException;
import wooteco.subway.web.member.InvalidAuthenticationException;
import wooteco.subway.web.member.NoSuchFavoriteException;
import wooteco.subway.web.member.NoSuchMemberException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> InvalidAuthenticationExceptionHandler(InvalidAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("비밀번호를 잘못 입력하셨습니다.", 401));
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> DuplicateMemberExceptionHandler(DuplicateMemberException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("중복되는 이메일이 있습니다.", 409));
    }

    @ExceptionHandler(NoSuchMemberException.class)
    public ResponseEntity<ErrorResponse> NoSuchMemberExceptionHandler(NoSuchMemberException e){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ErrorResponse("해당하는 멤버를 찾을 수 없습니다.", 204));
    }

    @ExceptionHandler(NoSuchFavoriteException.class)
    public ResponseEntity<ErrorResponse> NoSuchFavoriteExceptionHandler(NoSuchFavoriteException e){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ErrorResponse("해당하는 즐겨찾기를 찾을 수 없습니다.", 204));
    }
    
    @ExceptionHandler(NoSuchStationException.class)
    public ResponseEntity<ErrorResponse> NoSuchStationExceptionHandler(NoSuchStationException e){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ErrorResponse("해당하는 역을 찾을 수 없습니다.", 204));
    }
}
