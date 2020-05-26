package wooteco.subway.web.dto;

public class DefaultResponse<T> {
    private T data;
    private Integer code;
    private String message;

    private DefaultResponse() {
    }

    public DefaultResponse(final T data, final Integer code, final String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <Void> DefaultResponse<Void> empty() {
        return new DefaultResponse<>(null, null, null);
    }

    public static <Void> DefaultResponse<Void> message(String message) {
        return new DefaultResponse<>(null, null, message);
    }

    public static <T> DefaultResponse<T> of(T data) {
        return new DefaultResponse<>(data, null, null);
    }

    public static <T> DefaultResponse<T> of(T data, String message) {
        return new DefaultResponse<>(data, null, message);
    }

    public static <Void> DefaultResponse<Void> error(ErrorCode errorCode) {
        return new DefaultResponse<>(null, errorCode.getCode(), errorCode.getMessage());
    }

    public T getData() {
        return data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
