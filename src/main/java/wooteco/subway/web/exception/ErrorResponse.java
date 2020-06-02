package wooteco.subway.web.exception;

public class ErrorResponse {

    private static final String DEFAULT_MESSAGE = "알 수 없는 에러가 발생했습니다.";

    private String message;

    public ErrorResponse() {
        this.message = DEFAULT_MESSAGE;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
