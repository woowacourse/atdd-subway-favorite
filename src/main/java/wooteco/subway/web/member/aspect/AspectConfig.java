package wooteco.subway.web.member.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
	@Bean
	public MemberValidateAspect validateAspect() {
		return new MemberValidateAspect();
	}
}
