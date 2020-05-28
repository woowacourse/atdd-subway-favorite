package wooteco.subway.web.member.auth;

import static org.assertj.core.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import wooteco.subway.infra.JwtTokenProvider;

class BearerAuthenticationTest {
    @DisplayName("전달 받은 request에서 추출한 토큰이 validation 통과 실패시 exception")
    @Test
    void setAuthentication() {
        BearerAuthentication bearerAuthentication = new BearerAuthentication(
            (request, tokenProvider) -> "token", new JwtTokenProvider("1", 2));

        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        assertThatThrownBy(() -> bearerAuthentication.setAuthentication(httpServletRequest))
            .isInstanceOf(InvalidAuthenticationException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("request에 set한 인증정보가 존재하지 않을 경우 Exception 발생")
    @Test
    void getAuthentication() {
        BearerAuthentication bearerAuthentication = new BearerAuthentication(
            new AuthorizationExtractor(), new JwtTokenProvider("1", 1));

        NativeWebRequest nativeWebRequest = new ServletWebRequest(new MockHttpServletRequest());
        assertThatThrownBy(
            () -> bearerAuthentication.getAuthentication(nativeWebRequest, String.class))
            .isInstanceOf(InvalidAuthenticationException.class)
            .hasMessage("인증 정보가 비어있습니다.");
    }
}