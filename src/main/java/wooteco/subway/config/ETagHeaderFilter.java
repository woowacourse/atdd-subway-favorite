package wooteco.subway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class ETagHeaderFilter {
    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>(
            new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/lines/detail");
        filterRegistrationBean.setName("etagFilter");
        return filterRegistrationBean;
    }
}
