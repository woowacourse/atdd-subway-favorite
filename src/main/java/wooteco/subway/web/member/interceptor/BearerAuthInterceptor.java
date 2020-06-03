package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.common.AuthorizationType;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {

	private final AuthorizationExtractor authExtractor;
	private final JwtTokenProvider jwtTokenProvider;

	public BearerAuthInterceptor(AuthorizationExtractor authExtractor,
		JwtTokenProvider jwtTokenProvider) {
		this.authExtractor = authExtractor;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
		HttpServletResponse response, Object handler) {
		if (isCreateMember(request)) {
			return true;
		}
		String authHeaderValue = authExtractor
			.extract(request, AuthorizationType.BEARER.getPrefix());
		if (authHeaderValue.isEmpty()) {
			throw new InvalidAuthenticationException("로그인이 되지 않았습니다.");
		}
		if (isInvalidToken(authHeaderValue)) {
			throw new InvalidAuthenticationException("유효하지 않은 토큰입니다.");
		}
		String email = jwtTokenProvider.getSubject(authHeaderValue);

		request.setAttribute("loginMemberEmail", email);
		return true;
	}

	private boolean isInvalidToken(String authHeaderValue) {
		return !jwtTokenProvider.validateToken(authHeaderValue);
	}

	private boolean isCreateMember(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase("POST")
			&& request.getRequestURI().equals("/members");
	}
}
