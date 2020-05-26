package wooteco.subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.web.member.argumentresolver.FavoriteMethodArgumentResolver;
import wooteco.subway.web.member.argumentresolver.LoginMemberMethodArgumentResolver;
import wooteco.subway.web.member.argumentresolver.UpdateMemberMethodArgumentResolver;
import wooteco.subway.web.member.interceptor.BearerAuthFavoriteInterceptor;
import wooteco.subway.web.member.interceptor.BearerAuthMemberInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UpdateMemberMethodArgumentResolver updateMemberMethodArgumentResolver;
    private final BearerAuthMemberInterceptor bearerAuthMemberInterceptor;
    private final BearerAuthFavoriteInterceptor bearerAuthFavoriteInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;
    private final FavoriteMethodArgumentResolver favoriteMethodArgumentResolver;

    public WebMvcConfig(UpdateMemberMethodArgumentResolver updateMemberMethodArgumentResolver,
                        BearerAuthMemberInterceptor bearerAuthMemberInterceptor,
                        BearerAuthFavoriteInterceptor bearerAuthFavoriteInterceptor,
                        LoginMemberMethodArgumentResolver loginMemberArgumentResolver,
                        FavoriteMethodArgumentResolver favoriteMethodArgumentResolver) {
        this.updateMemberMethodArgumentResolver = updateMemberMethodArgumentResolver;
        this.bearerAuthMemberInterceptor = bearerAuthMemberInterceptor;
        this.bearerAuthFavoriteInterceptor = bearerAuthFavoriteInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.favoriteMethodArgumentResolver = favoriteMethodArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthMemberInterceptor).addPathPatterns("/members", "/members/*");
        registry.addInterceptor(bearerAuthFavoriteInterceptor).addPathPatterns("/favorites", "/favorites/*", "/favorites*");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(updateMemberMethodArgumentResolver);
        argumentResolvers.add(loginMemberArgumentResolver);
        argumentResolvers.add(favoriteMethodArgumentResolver);
    }
}
