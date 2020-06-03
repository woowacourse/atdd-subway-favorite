package wooteco.subway.web.member;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.Role;
import wooteco.subway.service.member.MemberService;

@Component
public class LoginMemberMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    public LoginMemberMethodArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = (String) webRequest.getAttribute("loginMemberEmail", SCOPE_REQUEST);
        try {
            Member member = memberService.findMemberByEmail(email);
            Role role = Objects.requireNonNull(parameter.getParameterAnnotation(LoginMember.class))
                    .role();
            if (role.isHigherThan(member.getRole())) {
                throw new InvalidAuthenticationException("권한이 없습니다.");
            }
            return member;
        } catch (Exception e) {
            throw new InvalidAuthenticationException("비정상적인 로그인");
        }
    }
}
