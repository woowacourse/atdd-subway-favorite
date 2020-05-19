package wooteco.subway.web.member.interceptor;

import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BasicAuthInterceptor implements HandlerInterceptor {
    private AuthorizationExtractor authExtractor;
    private MemberService memberService;

    public BasicAuthInterceptor(AuthorizationExtractor authExtractor, MemberService memberService) {
        this.authExtractor = authExtractor;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // TODO: Authorization 헤더를 통해 Basic 값을 추출 (authExtractor.extract() 메서드 활용)
        request.getHeader("Authorization");
        String credentials = authExtractor.extract(request,"Basic");
        if (StringUtils.isEmpty(credentials)) {
            return true;
        }

        // TODO: 추출한 Basic 값을 Base64를 통해 email과 password 값 추출(Base64.getDecoder().decode() 메서드 활용)
        byte[] decodedBytes = Base64.getDecoder().decode(credentials);
        String decodedString = new String(decodedBytes);

        String email = decodedString.split(":")[0];
        String password = decodedString.split(":")[1];

        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new InvalidAuthenticationException("올바르지 않은 이메일과 비밀번호 입력");
        }

        request.setAttribute("loginMemberEmail", email);
        return true;
    }
}
