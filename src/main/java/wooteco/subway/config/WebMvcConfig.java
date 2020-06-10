package wooteco.subway.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import wooteco.subway.web.BearerAuthInterceptor;
import wooteco.subway.web.LoginMemberMethodArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final BearerAuthInterceptor bearerAuthInterceptor;
	private final LoginMemberMethodArgumentResolver loginMemberArgumentResolver;

	public WebMvcConfig(BearerAuthInterceptor bearerAuthInterceptor,
		LoginMemberMethodArgumentResolver loginMemberArgumentResolver) {
		this.bearerAuthInterceptor = bearerAuthInterceptor;
		this.loginMemberArgumentResolver = loginMemberArgumentResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/me", "/me/*");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(loginMemberArgumentResolver);
	}

}
