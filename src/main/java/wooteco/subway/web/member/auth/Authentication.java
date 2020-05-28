package wooteco.subway.web.member.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.NativeWebRequest;

public interface Authentication {
    void setAuthentication(HttpServletRequest request);

    <T> T getAuthentication(NativeWebRequest request, Class<T> tClass);
}
