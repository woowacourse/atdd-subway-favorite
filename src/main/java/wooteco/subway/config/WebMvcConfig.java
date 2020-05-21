package wooteco.subway.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import wooteco.subway.web.member.LoginMemberMethodArgumentResolver;
import wooteco.subway.web.member.interceptor.BasicAuthInterceptor;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BasicAuthInterceptor basicAuthInterceptor;
    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;

    public WebMvcConfig(BasicAuthInterceptor basicAuthInterceptor,
        BearerAuthInterceptor bearerAuthInterceptor,
        LoginMemberMethodArgumentResolver loginMemberArgumentResolver) {
        this.basicAuthInterceptor = basicAuthInterceptor;
        this.bearerAuthInterceptor = bearerAuthInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthInterceptor).addPathPatterns("/me/basic");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/me/bearer").addPathPatterns("/members/*");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}
