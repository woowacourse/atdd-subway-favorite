package wooteco.subway.web.member.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;
import wooteco.subway.web.member.exception.NotMatchedEmailIExistInJwtException;
import wooteco.subway.web.member.util.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;

@Component
public class BearerAuthMemberInterceptor implements HandlerInterceptor {
    private AuthorizationExtractor authExtractor;
    private JwtTokenProvider jwtTokenProvider;

    public BearerAuthMemberInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        System.out.println("인터셉터 들어감");
        if (isPost(request)) {
            System.out.println("왜 일로 들어올까");
            return true;
        }

        String bearer = authExtractor.extract(request, "Bearer");
        System.out.println("이게 널일까요? " + bearer);
        validateToken(bearer);
        String email = jwtTokenProvider.getSubject(bearer);
        request.setAttribute("requestMemberEmail", email);

        if (isGet(request)) {
            validateEmailEquals(request, email);
            return true;
        }

        if (isPut(request) || isDelete(request)) {
            final Map<String, String> pathVariables = (Map<String, String>) request
                    .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            String id = pathVariables.get("id");
            request.setAttribute("requestId", id);
        }
        return true;
    }

    private boolean isDelete(HttpServletRequest request) {
        return DELETE.matches(request.getMethod());
    }

    private boolean isPut(HttpServletRequest request) {
        return PUT.matches(request.getMethod());
    }

    private void validateEmailEquals(HttpServletRequest request, String email) {
        System.out.println(request + " requuuuuu");
        System.out.println(email);
        System.out.println(request.getAttribute("requestMemberEmail"));
        if (request.getAttribute("requestMemberEmail").equals(email) == false) {
            throw new NotMatchedEmailIExistInJwtException(email);
        }
    }

    private boolean isGet(HttpServletRequest request) {
        return GET.matches(request.getMethod());
    }

    private boolean isPost(HttpServletRequest request) {
        return POST.matches(request.getMethod());
    }

    private void validateToken(String bearer) {
        if (bearer.isEmpty() || !jwtTokenProvider.validateToken(bearer)) {
            throw new InvalidAuthenticationException();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
