package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
		String credential = authExtractor.extract(request, "bearer");

		if (!jwtTokenProvider.validateToken(credential)) {
			throw new InvalidAuthenticationException("유효하지 않은 토큰입니다.");
		}
		String email = jwtTokenProvider.getSubject(credential);

		request.setAttribute("loginMemberEmail", email);
		return true;
	}
}
