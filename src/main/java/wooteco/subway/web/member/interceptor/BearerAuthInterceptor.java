package wooteco.subway.web.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
	private static final String TOKEN_TYPE = "bearer";
	private static final String EMAIL_ATTRIBUTE = "loginMemberEmail";

	private final AuthorizationExtractor authExtractor;
	private final JwtTokenProvider jwtTokenProvider;

	public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
		this.authExtractor = authExtractor;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
		HttpServletResponse response, Object handler) {
		final String bearer = authExtractor.extract(request, TOKEN_TYPE);
		if (!jwtTokenProvider.validateToken(bearer)) {
			throw new InvalidAuthenticationException("유효하지 않은 토큰값입니다.");
		}
		String email = jwtTokenProvider.getSubject(bearer);

		request.setAttribute(EMAIL_ATTRIBUTE, email);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
		HttpServletResponse response,
		Object handler,
		ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws Exception {

	}
}
