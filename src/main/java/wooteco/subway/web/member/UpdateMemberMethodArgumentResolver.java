package wooteco.subway.web.member;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.exception.UnAuthorizationException;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class UpdateMemberMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    public UpdateMemberMethodArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UpdateMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        System.out.println("여기 로직 안타 ? ");
        String id = (String) webRequest.getAttribute("requestId", SCOPE_REQUEST);
        String email = (String) webRequest.getAttribute("requestMemberEmail", SCOPE_REQUEST);

        Member updateMember = memberService.findMemberById(Long.valueOf(id));
        if (updateMember.isNotEqualEmail(email)) {
            throw new UnAuthorizationException(email);
        }
        return updateMember;
    }
}
