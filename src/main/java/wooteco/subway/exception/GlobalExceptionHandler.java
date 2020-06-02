package wooteco.subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.SubwayAdminApplication;
import wooteco.subway.service.station.NoSuchStationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String SYSTEM_ERROR_MESSAGE = "시스템 오류";
    private static final Logger LOGGER = LoggerFactory.getLogger(SubwayAdminApplication.class);

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<String> InvalidAuthenticationExceptionHandler(InvalidAuthenticationException e) {
        LOGGER.error("error >>> {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<String> DuplicateMemberExceptionHandler(DuplicateMemberException e) {
        LOGGER.error("error >>> {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchMemberException.class)
    public ResponseEntity<String> NoSuchMemberExceptionHandler(NoSuchMemberException e) {
        LOGGER.error("error >>> {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchFavoriteException.class)
    public ResponseEntity<String> NoSuchFavoriteExceptionHandler(NoSuchFavoriteException e) {
        LOGGER.error("error >>> {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchStationException.class)
    public ResponseEntity<String> NoSuchStationExceptionHandler(NoSuchStationException e) {
        LOGGER.error("error >>> {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> ExceptionHandler(Exception e) {
        LOGGER.error("error >>> {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SYSTEM_ERROR_MESSAGE);
    }
}
