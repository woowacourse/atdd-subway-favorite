package wooteco.subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.web.member.argumentresolver.LoginMemberMethodArgumentResolver;
import wooteco.subway.web.member.interceptor.BearerAuthMemberInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BearerAuthMemberInterceptor bearerAuthMemberInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;

    public WebMvcConfig(BearerAuthMemberInterceptor bearerAuthMemberInterceptor, LoginMemberMethodArgumentResolver loginMemberArgumentResolver) {
        this.bearerAuthMemberInterceptor = bearerAuthMemberInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthMemberInterceptor)
                .addPathPatterns("/auth/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}
