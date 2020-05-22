package wooteco.subway.web.member;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {

    public String extract(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals("token"))
            .map(Cookie::getValue)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("토큰을 찾을 수 없습니다."));
    }
}
