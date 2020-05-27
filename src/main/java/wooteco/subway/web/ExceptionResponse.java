package wooteco.subway.web;

public class ExceptionResponse {
    private final String errorSign;

    public ExceptionResponse(final String errorSign) {
        this.errorSign = errorSign;
    }

    public String getErrorSign() {
        return errorSign;
    }
}
