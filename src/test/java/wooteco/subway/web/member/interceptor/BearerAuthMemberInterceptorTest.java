package wooteco.subway.web.member.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;
import wooteco.subway.web.member.util.AuthorizationExtractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static wooteco.subway.DummyTestUserInfo.EMAIL;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BearerAuthMemberInterceptorTest {

    @Mock
    private AuthorizationExtractor authExtractor;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("성공적인 attribute 세팅")
    @Test
    void preHandle() throws InvalidAuthenticationException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String token = "토큰";
        given(authExtractor.extract(request, "Bearer")).willReturn(token);
        given(jwtTokenProvider.validateToken(token)).willReturn(true);
        given(jwtTokenProvider.getSubject(token)).willReturn(EMAIL);
        BearerAuthMemberInterceptor bearerAuthMemberInterceptor =
                new BearerAuthMemberInterceptor(authExtractor, jwtTokenProvider);

        bearerAuthMemberInterceptor.preHandle(request, response, "핸들러");

        assertThat(request.getAttribute("requestMemberEmail")).isEqualTo(EMAIL);
    }

    @DisplayName("토큰 값이 비었을 때")
    @Test
    void preHandleFail1() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        given(authExtractor.extract(request, "Bearer")).willReturn("");
        BearerAuthMemberInterceptor bearerAuthMemberInterceptor =
                new BearerAuthMemberInterceptor(authExtractor, jwtTokenProvider);

        assertThatThrownBy(() -> bearerAuthMemberInterceptor.preHandle(request, response, "핸들러"))
                .isInstanceOf(InvalidAuthenticationException.class);
    }
}