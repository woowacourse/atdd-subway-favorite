package wooteco.subway.web.member.argumentresolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LoginMemberMethodArgumentResolverTest {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Mock
    private MemberService memberService;

    @Mock
    private MethodParameter methodParameter;

    @Mock
    private ModelAndViewContainer modelAndViewContainer;

    @Mock
    private WebDataBinderFactory webDataBinderFactory;

    @Test
    @DisplayName("매개변수 전환")
    void argumentSuccessResolve() throws InvalidAuthenticationException {
        String requestEmail = "coyle@gmail.com";
        DispatcherServletWebRequest dispatcherServletWebRequest = new DispatcherServletWebRequest(httpServletRequest);
        dispatcherServletWebRequest.setAttribute("requestMemberEmail", requestEmail, SCOPE_REQUEST);

        given(memberService.findMemberByEmail(requestEmail))
                .willReturn(new Member(1L, requestEmail, "coyle", "1234"));
        LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver = new LoginMemberMethodArgumentResolver(memberService);
        Object o = loginMemberMethodArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, dispatcherServletWebRequest, webDataBinderFactory);
        assertThat(o.getClass() == Member.class).isTrue();
    }
}