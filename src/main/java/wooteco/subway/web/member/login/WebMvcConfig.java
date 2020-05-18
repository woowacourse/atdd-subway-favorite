package wooteco.subway.web.member.login;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    private final BasicAuthInterceptor basicAuthInterceptor;
    private final SessionInterceptor sessionInterceptor;
    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;

    public WebMvcConfig(BasicAuthInterceptor basicAuthInterceptor, SessionInterceptor sessionInterceptor, BearerAuthInterceptor bearerAuthInterceptor, LoginMemberMethodArgumentResolver loginMemberArgumentResolver) {
        this.basicAuthInterceptor = basicAuthInterceptor;
        this.sessionInterceptor = sessionInterceptor;
        this.bearerAuthInterceptor = bearerAuthInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthInterceptor).addPathPatterns("/me/basic");
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/me/session");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/me/bearer");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}
