package wooteco.subway.web.exceptionhandler;

import java.util.Optional;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;

public class ExceptionResponse {
    private final String message;

    public ExceptionResponse(FieldError fieldError) {
        Optional<FieldError> fieldError1 = Optional.ofNullable(fieldError);

        this.message = fieldError1.map(DefaultMessageSourceResolvable::getDefaultMessage)
            .orElseGet(() -> "알수 없는 에러가 발생했습니다.");
    }

    public ExceptionResponse(String string){
        this.message = string;
    }

    public String getMessage() {
        return message;
    }
}
