package wooteco.subway.web.member.interceptor;

import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

public interface Authentication {
    void setAuthentication(HttpServletRequest request);

    <T> T getAuthentication(NativeWebRequest request, Class<T> tClass);
}
