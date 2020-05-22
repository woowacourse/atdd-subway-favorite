package wooteco.subway.web.member.auth;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

@Profile(value = "test")
@Component
public class StubAuthentication implements Authentication {
    @Override
    public void setAuthentication(final HttpServletRequest request) {

    }

    @Override
    public <T> T getAuthentication(final NativeWebRequest request, final Class<T> tClass) {
        return (T) "pci2676@gmail.com";
    }
}
