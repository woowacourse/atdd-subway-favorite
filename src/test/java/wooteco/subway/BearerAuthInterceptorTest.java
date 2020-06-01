package wooteco.subway;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.BearerAuthInterceptor;
import wooteco.subway.web.InvalidAuthenticationException;

@SpringBootTest
public class BearerAuthInterceptorTest {
	@Autowired
	private BearerAuthInterceptor interceptor;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@Test
	void preHandle_EmptyToken() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
			.isInstanceOf(InvalidAuthenticationException.class)
			.hasMessage("Token이 존재하지 않습니다.");
	}

	@Test
	void preHandle_InvalidToken() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer this.is.token");
		MockHttpServletResponse response = new MockHttpServletResponse();

		assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
			.isInstanceOf(InvalidAuthenticationException.class)
			.hasMessage("Token이 잘못되었습니다.");
	}

	@Test
	void preHandle_ValidToken() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer this.is.token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		given(jwtTokenProvider.validateToken(any())).willReturn(true);

		assertThat(interceptor.preHandle(request, response, null)).isTrue();
	}
}
