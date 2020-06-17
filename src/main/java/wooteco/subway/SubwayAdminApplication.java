package wooteco.subway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.config.TokenConfig;

@SpringBootApplication
@EnableConfigurationProperties(TokenConfig.class)
@Transactional(propagation = Propagation.REQUIRED)
public class SubwayAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(SubwayAdminApplication.class, args);
	}
}