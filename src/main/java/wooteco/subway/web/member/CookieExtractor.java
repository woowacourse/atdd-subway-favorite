package wooteco.subway.web.member;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import wooteco.subway.exception.CustomException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

@Component
public class CookieExtractor {
    public String extract(HttpServletRequest request, String type) {
        Cookie[] cookies = request.getCookies();

        if (Objects.isNull(cookies)) {
            throw new CustomException(new IllegalArgumentException("Token이 존재하지 않습니다."));
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(type))
                .map(Cookie::getValue)
                .findAny()
                .orElse(Strings.EMPTY);
    }
}
