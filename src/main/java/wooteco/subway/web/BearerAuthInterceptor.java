package wooteco.subway.web;

import static wooteco.subway.web.LoginMemberMethodArgumentResolver.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.infra.JwtTokenProvider;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {

	public static final String BEARER_TOKEN = "Bearer";

	private final AuthorizationExtractor authExtractor;
	private final JwtTokenProvider jwtTokenProvider;

	public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
		this.authExtractor = authExtractor;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
		HttpServletResponse response, Object handler) {
		String token = authExtractor.extract(request, BEARER_TOKEN);

		if (StringUtils.isEmpty(token)) {
			throw new InvalidAuthenticationException("Token이 존재하지 않습니다.");
		}

		if (!jwtTokenProvider.validateToken(token)) {
			throw new InvalidAuthenticationException("Token이 잘못되었습니다.");
		}

		String email = jwtTokenProvider.getSubject(token);
		request.setAttribute(MEMBER_EMAIL_ATTRIBUTE, email);

		return true;
	}

}
