package wooteco.subway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import wooteco.subway.exception.CustomException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.BearerAuthInterceptor;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class BearerAuthInterceptorTest {
    @Autowired
    private BearerAuthInterceptor interceptor;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void preHandle_NullToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> interceptor.preHandle(request, new MockHttpServletResponse(), null))
                .isInstanceOf(CustomException.class)
                .hasMessage("Token이 존재하지 않습니다.");
    }

    @Test
    void preHandle_EmptyToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("Bearer", ""));

        assertThatThrownBy(() -> interceptor.preHandle(request, new MockHttpServletResponse(), null))
                .isInstanceOf(CustomException.class)
                .hasMessage("Token이 존재하지 않습니다.");
    }

    @Test
    void preHandle_InvalidToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("Bearer", "Bearer this.is.token"));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
                .isInstanceOf(CustomException.class)
                .hasMessage("Token이 잘못되었습니다.");
    }

    @Test
    void preHandle_ValidToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("Bearer", "This.is.token");
        cookie.setHttpOnly(true);
        request.setCookies(cookie);

        given(jwtTokenProvider.validateToken(any())).willReturn(true);

        assertThat(interceptor.preHandle(request, new MockHttpServletResponse(), null)).isTrue();
    }
}
