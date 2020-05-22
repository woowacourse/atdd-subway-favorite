package wooteco.subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.web.member.LoginMemberMethodArgumentResolver;
import wooteco.subway.web.member.UpdateMemberMethodArgumentResolver;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UpdateMemberMethodArgumentResolver updateMemberMethodArgumentResolver;
    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;

    public WebMvcConfig(UpdateMemberMethodArgumentResolver updateMemberMethodArgumentResolver, BearerAuthInterceptor bearerAuthInterceptor, LoginMemberMethodArgumentResolver loginMemberArgumentResolver) {
        this.updateMemberMethodArgumentResolver = updateMemberMethodArgumentResolver;
        this.bearerAuthInterceptor = bearerAuthInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/members", "/members/*");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(updateMemberMethodArgumentResolver);
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}
