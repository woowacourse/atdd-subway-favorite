package wooteco.subway.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionResponse {
    private String errorMessage;

    public ExceptionResponse(@JsonProperty("errorMessage") String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
