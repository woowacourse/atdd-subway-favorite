package wooteco.subway.web.exception;

public class NoSuchValueException extends SubwayException {
    public static final String NO_SUCH_MEMBER_MESSAGE = "존재하지 않는 사용자입니다.";
    public static final String NO_SUCH_FAVORITE_MESSAGE = "존재하지 않는 즐겨찾기입니다.";
    public static final String NO_SUCH_LINE_MESSAGE = "존재하지 않는 노선입니다.";
    public static final String NO_SUCH_STATION_MESSAGE = "존재하지 않는 역입니다.";
    public static final String NO_SUCH_LINE_STATION_MESSAGE = "존재하지 않는 구간입니다.";

    public NoSuchValueException(String message) {
        super(message);
    }
}
