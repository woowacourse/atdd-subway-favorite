package wooteco.subway.web.member;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.authentication.InvalidAuthenticationException;
import wooteco.subway.exception.authentication.TokenSessionNotMatchingException;
import wooteco.subway.service.member.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
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
        String email = validateAuthorization(webRequest);
        Member member = memberService.findMemberByEmail(email);
        validatePathVariable(webRequest, member);
        validateParam(webRequest, member);
        return member;
    }

    private String validateAuthorization(NativeWebRequest webRequest) {
        String emailJwt = (String) webRequest.getAttribute("loginMemberEmailJwt", SCOPE_REQUEST);
        String emailSession = (String) webRequest.getAttribute("loginMemberEmailSession", SCOPE_REQUEST);

        if (!Objects.equals(emailJwt, emailSession)) {
            throw new TokenSessionNotMatchingException();
        }

        return emailJwt;
    }

    private void validatePathVariable(NativeWebRequest webRequest, Member member) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String> pathParams = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String id = pathParams.get("id");

        if (Objects.nonNull(id)) {
            member.validateId(Long.parseLong(id));
        }
    }

    private void validateParam(NativeWebRequest webRequest, Member member) {
        String paramEmail = webRequest.getParameter("email");

        if (paramEmail != null
                && !paramEmail.isEmpty()
                && !Objects.equals(paramEmail, member.getEmail())) {
            throw new InvalidAuthenticationException("현재 로그인한 아이디 값에 대한 요청이 아닙니다");
        }
    }
}
