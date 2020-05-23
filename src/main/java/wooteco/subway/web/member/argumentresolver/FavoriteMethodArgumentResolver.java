package wooteco.subway.web.member.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.web.member.argumentresolver.annotation.RequestMember;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class FavoriteMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final FavoriteService favoriteService;

    public FavoriteMethodArgumentResolver(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = (String) webRequest.getAttribute("requestMemberEmail", SCOPE_REQUEST);
        String source = (String) webRequest.getAttribute("source", SCOPE_REQUEST);
        String target = (String) webRequest.getAttribute("target", SCOPE_REQUEST);
        Favorite favoriteBySourceAndTarget = favoriteService.findFavoriteBySourceAndTarget(source, target);
        System.out.println(favoriteBySourceAndTarget);
        if (favoriteBySourceAndTarget.isNotEqualEmail(email)) {
            throw new InvalidAuthenticationException("비정상적인 로그인");
        }
        return favoriteBySourceAndTarget;
    }
}
