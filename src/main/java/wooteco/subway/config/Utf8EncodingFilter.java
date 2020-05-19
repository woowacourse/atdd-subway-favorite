package wooteco.subway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class Utf8EncodingFilter {
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> utf8EncodingFilter() {
        return new FilterRegistrationBean<>(new CharacterEncodingFilter("UTF-8", true));
    }
}
