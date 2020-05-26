package wooteco.subway.web.prehandler;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import wooteco.subway.web.exception.InvalidTokenException;

@Component
public class AuthorizationExtractor {

    public String extract(HttpServletRequest request) {
        try {
            return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new InvalidTokenException("토큰을 찾을 수 없습니다."));
        } catch (Exception e) {
            throw new InvalidTokenException("토큰을 찾을 수 없습니다.");
        }
    }
}
