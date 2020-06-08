package wooteco.subway.web.member.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.argumentresolver.annotation.LoginMember;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;
import wooteco.subway.web.member.exception.NotExistMemberDataException;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

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
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws InvalidAuthenticationException {
        String email = (String) webRequest.getAttribute("requestMemberEmail", SCOPE_REQUEST);
        try {
            return memberService.findMemberByEmail(email);
        } catch (NotExistMemberDataException e) {
            throw new InvalidAuthenticationException("해당 정보를 가진 유저가 존재하지 않습니다.");
        }
    }
}
