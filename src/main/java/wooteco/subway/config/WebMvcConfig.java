package wooteco.subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;
import wooteco.subway.web.member.resolver.LoginMemberMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

    public WebMvcConfig(final BearerAuthInterceptor bearerAuthInterceptor, final LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
        this.loginMemberMethodArgumentResolver = loginMemberMethodArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/me").addPathPatterns("/me/*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginMemberMethodArgumentResolver);
    }

}
