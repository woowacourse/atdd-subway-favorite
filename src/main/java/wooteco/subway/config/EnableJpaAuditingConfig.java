package wooteco.subway.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
// @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class EnableJpaAuditingConfig {
}
