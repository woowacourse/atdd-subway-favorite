package wooteco.subway.domain.favoritepath;

public class RegisterFavoritePathException extends RuntimeException {
    public static final String ALREADY_REGISTERED_MESSAGE = "이름이 비어있을 수 없습니다.";
    public RegisterFavoritePathException(String message) {
        super(message);
    }
}
