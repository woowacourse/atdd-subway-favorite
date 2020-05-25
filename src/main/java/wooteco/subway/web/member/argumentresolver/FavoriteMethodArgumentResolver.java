package wooteco.subway.web.member.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.argumentresolver.annotation.CreateFavorite;
import wooteco.subway.web.member.exception.InvalidAuthenticationException;
import wooteco.subway.web.member.exception.NotExistFavoriteDataException;
import wooteco.subway.web.member.exception.NotExistMemberDataException;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class FavoriteMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final FavoriteService favoriteService;
    private final MemberService memberService;

    public FavoriteMethodArgumentResolver(FavoriteService favoriteService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CreateFavorite.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        System.out.println("리졸버에 들어옵니다");
        String email = (String) webRequest.getAttribute("requestMemberEmail", SCOPE_REQUEST);
        validateMemberExist(email);

        String source = (String) webRequest.getAttribute("source", SCOPE_REQUEST);
        String target = (String) webRequest.getAttribute("target", SCOPE_REQUEST);

        Favorite favoriteBySourceAndTarget;
        try {
            favoriteBySourceAndTarget = favoriteService.findFavoriteBySourceAndTarget(source, target);
        } catch(NotExistFavoriteDataException e) {
            return new Favorite(source, target, email);
        }

        if (favoriteBySourceAndTarget.isNotEqualEmail(email)) {
            throw new InvalidAuthenticationException("비정상적인 로그인");
        }
        System.out.println("리졸버에 나갑니다");
        return favoriteBySourceAndTarget;
    }

    private void validateMemberExist(String email) {
        try {
            memberService.findMemberByEmail(email);
        } catch(NotExistMemberDataException e) {
            throw new InvalidAuthenticationException();
        }
    }
}
