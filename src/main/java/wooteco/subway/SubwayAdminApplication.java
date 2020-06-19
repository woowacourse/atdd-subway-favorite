package wooteco.subway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SubwayAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubwayAdminApplication.class, args);
    }
}
