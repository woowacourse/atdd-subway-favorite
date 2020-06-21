package woowa.bossdog.subway.web.advice;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionResponse {
    private String errorCode;
    private String message;

    public ExceptionResponse(final String errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
