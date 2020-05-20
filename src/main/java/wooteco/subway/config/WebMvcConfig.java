package wooteco.subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.web.member.LoginMemberMethodArgumentResolver;
import wooteco.subway.web.member.RegisterMemberMethodArgumentResolver;
import wooteco.subway.web.member.interceptor.BasicAuthInterceptor;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BasicAuthInterceptor basicAuthInterceptor;
    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;
    private final RegisterMemberMethodArgumentResolver registerMemberMethodArgumentResolver;

    public WebMvcConfig(BasicAuthInterceptor basicAuthInterceptor,
        BearerAuthInterceptor bearerAuthInterceptor,
        LoginMemberMethodArgumentResolver loginMemberArgumentResolver,
        RegisterMemberMethodArgumentResolver registerMemberMethodArgumentResolver) {
        this.basicAuthInterceptor = basicAuthInterceptor;
        this.bearerAuthInterceptor = bearerAuthInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.registerMemberMethodArgumentResolver = registerMemberMethodArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthInterceptor).addPathPatterns("/me/basic");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/me/bearer");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
        argumentResolvers.add(registerMemberMethodArgumentResolver);
    }
}
