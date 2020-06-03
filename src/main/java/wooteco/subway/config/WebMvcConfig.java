package wooteco.subway.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.web.member.LoginMemberMethodArgumentResolver;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;
import wooteco.subway.web.member.interceptor.MemberApiInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MemberApiInterceptor memberApiInterceptor;
    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;

    public WebMvcConfig(MemberApiInterceptor memberApiInterceptor,
            BearerAuthInterceptor bearerAuthInterceptor,
            LoginMemberMethodArgumentResolver loginMemberArgumentResolver) {
        this.memberApiInterceptor = memberApiInterceptor;
        this.bearerAuthInterceptor = bearerAuthInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberApiInterceptor).addPathPatterns("/members/**");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/me/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}
