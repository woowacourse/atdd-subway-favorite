package wooteco.subway.web.member;

import static org.springframework.web.context.request.RequestAttributes.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import wooteco.subway.exception.InvalidAuthenticationException;
import wooteco.subway.service.member.MemberService;

@Component
public class LoginMemberMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String LOGIN_MEMBER_ATTRIBUTE_NAME = "loginMemberEmail";

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
        String email = (String)webRequest.getAttribute(LOGIN_MEMBER_ATTRIBUTE_NAME, SCOPE_REQUEST);
        if (StringUtils.isBlank(email)) {
            throw new InvalidAuthenticationException("로그인이 되어있지 않거나 세션이 만료되었습니다.");
        }
        try {
            return memberService.findMemberByEmail(email);
        } catch (Exception e) {
            throw new InvalidAuthenticationException("비정상적인 로그인");
        }
    }
}
