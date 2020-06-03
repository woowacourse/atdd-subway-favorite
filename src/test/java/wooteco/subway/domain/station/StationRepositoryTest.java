package wooteco.subway.domain.station;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import wooteco.subway.config.EnableJdbcAuditingConfig;

@DataJdbcTest
@Import(EnableJdbcAuditingConfig.class)
public class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;

    @Test
    void saveStation() {
        String stationName = "강남역";
        stationRepository.save(new Station(stationName));

        assertThrows(DbActionExecutionException.class, () -> stationRepository.save(new Station(stationName)));
    }

    @Test
    void createDate() {
        String stationName = "강남역";
        Station persistStation = stationRepository.save(new Station(stationName));

        assertThat(persistStation.getCreatedAt()).isNotNull();
    }
}
