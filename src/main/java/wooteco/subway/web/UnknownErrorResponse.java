package wooteco.subway.web;

public class UnknownErrorResponse {

    private final String message;

    public UnknownErrorResponse(String message) {
        this.message = message;
    }

    public static UnknownErrorResponse of(Exception e) {
        return new UnknownErrorResponse(e.getMessage());
    }

    public String getMessage() {
        return this.message;
    }
}
