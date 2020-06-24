package wooteco.subway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SubwayAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubwayAdminApplication.class, args);
    }

}
