package wooteco.subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.web.member.login.BearerAuthInterceptor;
import wooteco.subway.web.member.login.LoginMemberMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;

    public WebMvcConfig(BearerAuthInterceptor bearerAuthInterceptor, LoginMemberMethodArgumentResolver loginMemberArgumentResolver) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/lines/**")
                .excludePathPatterns("/stations/**")
                .excludePathPatterns("/members/**")
                .excludePathPatterns("/paths")
                .excludePathPatterns("/edges")
                .excludePathPatterns("/map")
                .excludePathPatterns("/search")
                .excludePathPatterns("/join")
                .excludePathPatterns("/login")
                .excludePathPatterns("/mypage")
                .excludePathPatterns("/edit")
                .excludePathPatterns("/oauth/token")
                .excludePathPatterns("/service/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}
