package wooteco.subway.web.member.login;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.exception.InvalidAuthenticationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BearerAuthInterceptorTest {

    @Autowired
    private BearerAuthInterceptor bearerAuthInterceptor;
    @MockBean
    private AuthorizationExtractor authExtractor;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("토큰이 비어있을때 예외가 발생하는지 테스트")
    @Test
    void EmptyTokenTest() {

        when(authExtractor.extract(any(), any())).thenReturn("");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> bearerAuthInterceptor.preHandle(request, response, null))
                .isInstanceOf(InvalidAuthenticationException.class)
                .hasMessage("토큰이 비어있습니다.");
    }

    @DisplayName("비정상적인 토큰일때 예외가 발생하는지 테스트")
    @Test
    void invalidTokenTest() {

        when(authExtractor.extract(any(), any())).thenReturn("비정상적인 토큰");
        when(jwtTokenProvider.validateToken(any())).thenReturn(false);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> bearerAuthInterceptor.preHandle(request, response, null))
                .isInstanceOf(InvalidAuthenticationException.class)
                .hasMessage("비정상적인 토큰입니다.");
    }
}
