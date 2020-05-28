package wooteco.subway.web.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.InvalidAuthenticationException;
import wooteco.subway.exception.NoResourceExistException;
import wooteco.subway.service.member.MemberService;

import java.util.Objects;

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
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String emailJwt = (String) webRequest.getAttribute("loginMemberEmailJwt", SCOPE_REQUEST);
        String emailSession = (String) webRequest.getAttribute("loginMemberEmailSession", SCOPE_REQUEST);

        if (!Objects.equals(emailJwt, emailSession)) {
            throw new InvalidAuthenticationException("토큰정보와 세션정보가 일치하지 않습니다");
        }

        if (StringUtils.isBlank(emailJwt)) {
            return new Member();
        }
        try {
            return memberService.findMemberByEmail(emailJwt);
        } catch (NoResourceExistException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidAuthenticationException("이메일 인증 실패");
        }
    }
}
