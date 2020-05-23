//package wooteco.subway.web.advice;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import wooteco.subway.web.member.InvalidAuthenticationException;
//import wooteco.subway.web.member.exception.InvalidPasswordException;
//import wooteco.subway.web.member.exception.NotExistMemberDataException;
//import wooteco.subway.web.member.exception.NotMatchedEmailIExistInJwtException;
//
//@RestControllerAdvice
//public class AdviceController {
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//    }
//
//    @ExceptionHandler(InvalidAuthenticationException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidAuthenticationException(InvalidAuthenticationException e) {
//        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
//    }
//}
