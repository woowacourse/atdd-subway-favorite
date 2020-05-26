package wooteco.subway.web.member.interceptor;

import com.google.gson.Gson;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;
import wooteco.subway.web.member.util.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BearerAuthFavoriteInterceptor implements HandlerInterceptor {
    private static final Gson gson = new Gson();

    private AuthorizationExtractor authExtractor;
    private JwtTokenProvider jwtTokenProvider;

    public BearerAuthFavoriteInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Favorite 인터셉터 들어감");
        if(request.getMethod().equals(HttpMethod.POST.name())) {
            String content = request.getReader().readLine();
            CreateFavoriteRequest createFavoriteRequest = gson.fromJson(content, CreateFavoriteRequest.class);

            String source = createFavoriteRequest.getSource();
            request.setAttribute("source", source);

            String target = createFavoriteRequest.getTarget();
            request.setAttribute("target", target);
        }

        String bearer = authExtractor.extract(request, "Bearer");
        System.out.println(bearer + " 베어맨");
        validateToken(bearer);
        String email = jwtTokenProvider.getSubject(bearer);

        request.setAttribute("requestMemberEmail", email);

        return true;
    }

    private void validateToken(String bearer) {
        if (bearer.isEmpty() || !jwtTokenProvider.validateToken(bearer)) {
            System.out.println("에러 포인트 1");
            throw new InvalidAuthenticationException();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
