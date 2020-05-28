package wooteco.subway.web.member.auth;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor implements HeaderExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE =
        HeaderExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    @Override
    public String extract(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(type.toLowerCase()))) {
                String authHeaderValue = value.substring(type.length()).trim();
                request.setAttribute(ACCESS_TOKEN_TYPE, value.substring(0, type.length()).trim());
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }

        return Strings.EMPTY;
    }
}
