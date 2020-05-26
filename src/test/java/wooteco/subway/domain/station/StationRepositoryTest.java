package wooteco.subway.domain.station;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJdbcTest
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
    void findAllByIds() {
        stationRepository.save(new Station("서울"));
        stationRepository.save(new Station("강남"));
        stationRepository.save(new Station("잠실"));
        List<Long> ids = Arrays.asList(1L, 3L);

        List<Station> stations = stationRepository.findAllById(ids);

        assertThat(stations.size()).isEqualTo(2);
    }
}
