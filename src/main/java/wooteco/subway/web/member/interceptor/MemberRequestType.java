package wooteco.subway.web.member.interceptor;

import org.springframework.http.HttpMethod;
import wooteco.subway.web.exception.SubwayException;

import java.util.Arrays;
import java.util.regex.Pattern;

public enum MemberRequestType {
    CREATE_MEMBER_REQUEST(HttpMethod.POST.name(), Pattern.compile("/join"), false),
    LOGIN_REQUEST(HttpMethod.POST.name(), Pattern.compile("/login"), false),
    GET_MEMBER_REQUEST(HttpMethod.GET.name(), Pattern.compile("/members"), true),
    UPDATE_REQUEST(HttpMethod.PUT.name(), Pattern.compile("/members/[0-9]+"), true),
    DELETE_REQUEST(HttpMethod.DELETE.name(), Pattern.compile("/members/[0-9]+"), true),
    ADD_FAVORITE_REQUEST(HttpMethod.POST.name(), Pattern.compile("/members/favorites"), true),
    FIND_FAVORITE_REQUEST(HttpMethod.GET.name(), Pattern.compile("/members/favorites"), true),
    DELETE_FAVORITE_REQUEST(HttpMethod.DELETE.name(), Pattern.compile("/members/favorites/[0-9]+"), true);

    private final String httpMethod;
    private final Pattern uri;
    private final boolean hasToken;

    MemberRequestType(String httpMethod, Pattern uri, boolean hasToken) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.hasToken = hasToken;
    }

    public static boolean hasNoToken(String httpMethod, String uri) {
        MemberRequestType memberRequestType = Arrays.stream(values())
                .filter(val -> val.httpMethod.equals(httpMethod))
                .filter(val -> val.uri.matcher(uri).matches())
                .findFirst()
                .orElseThrow(() -> new SubwayException("처리할 수 없는 요청입니다."));
        return !memberRequestType.hasToken;
    }
}
