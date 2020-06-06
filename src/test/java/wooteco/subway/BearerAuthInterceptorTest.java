package wooteco.subway;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static wooteco.subway.web.AuthorizationExtractor.*;
import static wooteco.subway.web.BearerAuthInterceptor.*;

import org.junit.jupiter.api.BeforeEach;
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

	public static final String TEST_TOKEN_SECRET_KEY = " this.is.token";

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Autowired
	private BearerAuthInterceptor interceptor;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	void preHandle_EmptyToken() {
		assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
			.isInstanceOf(InvalidAuthenticationException.class)
			.hasMessage("Token이 존재하지 않습니다.");
	}

	@Test
	void preHandle_InvalidToken() {
		request.addHeader(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY);

		assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
			.isInstanceOf(InvalidAuthenticationException.class)
			.hasMessage("Token이 잘못되었습니다.");
	}

	@Test
	void preHandle_ValidToken() {
		request.addHeader(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY);
		given(jwtTokenProvider.validateToken(any())).willReturn(true);

		assertThat(interceptor.preHandle(request, response, null)).isTrue();
	}

}
