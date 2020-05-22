package wooteco.subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;
import wooteco.subway.web.member.resolver.LoginMemberMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Profile("prod")
    @Configuration
    public static class ProdWebMvcConfig implements WebMvcConfigurer {
        private final BearerAuthInterceptor bearerAuthInterceptor;
        private final LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

        public ProdWebMvcConfig(
                BearerAuthInterceptor bearerAuthInterceptor,
                LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver) {
            this.bearerAuthInterceptor = bearerAuthInterceptor;
            this.loginMemberMethodArgumentResolver = loginMemberMethodArgumentResolver;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/members**").addPathPatterns("/me");
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(loginMemberMethodArgumentResolver);
        }

    }

    @Profile("local")
    @Configuration
    public static class LocalWebMvcConfig implements WebMvcConfigurer {
        private final BearerAuthInterceptor bearerAuthInterceptor;
        private final LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

        public LocalWebMvcConfig(
                BearerAuthInterceptor bearerAuthInterceptor,
                LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver) {
            this.bearerAuthInterceptor = bearerAuthInterceptor;
            this.loginMemberMethodArgumentResolver = loginMemberMethodArgumentResolver;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/members**").addPathPatterns("/me");
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(loginMemberMethodArgumentResolver);
        }

        @Override
        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("file:src/main/resources/static/", "file:src/main/resources/templates/");
        }
    }


}
