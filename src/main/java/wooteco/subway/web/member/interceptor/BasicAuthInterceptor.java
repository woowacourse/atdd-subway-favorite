package wooteco.subway.web.member.interceptor;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

@Component
public class BasicAuthInterceptor implements HandlerInterceptor {
	private AuthorizationExtractor authExtractor;
	private MemberService memberService;

	public BasicAuthInterceptor(AuthorizationExtractor authExtractor, MemberService memberService) {
		this.authExtractor = authExtractor;
		this.memberService = memberService;
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
		final Object handler) {
		final byte[] decode = Base64.getDecoder().decode(authExtractor.extract(request, "basic"));
		final List<String> decodes = Arrays.asList(new String(decode).split(":"));

		final String email = decodes.get(0);
		final String password = decodes.get(1);

		final Member member = memberService.findMemberByEmail(email);
		if (!member.checkPassword(password)) {
			throw new InvalidAuthenticationException("올바르지 않은 이메일과 비밀번호 입력");
		}

		request.setAttribute("loginMemberEmail", email);
		return true;
	}
}
