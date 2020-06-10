package wooteco.subway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class Utf8EncodingFilter {
	private static final String ENCODING = "UTF-8";

	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> utf8CharacterEncodingFilter() {
		return new FilterRegistrationBean<>(new CharacterEncodingFilter(ENCODING, true));
	}
}
