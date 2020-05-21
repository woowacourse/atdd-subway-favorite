package wooteco.subway.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import wooteco.subway.web.member.LoginMemberMethodArgumentResolver;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;
import wooteco.subway.web.member.interceptor.MemberAuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;
    private final MemberAuthInterceptor memberAuthInterceptor;

    public WebMvcConfig(BearerAuthInterceptor bearerAuthInterceptor,
        LoginMemberMethodArgumentResolver loginMemberArgumentResolver,
        MemberAuthInterceptor memberAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.memberAuthInterceptor = memberAuthInterceptor;

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberAuthInterceptor).addPathPatterns("/me/bearer");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/members/{*id}");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}
