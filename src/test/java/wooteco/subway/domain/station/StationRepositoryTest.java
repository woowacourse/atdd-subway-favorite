package wooteco.subway.domain.station;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StationRepositoryTest {
    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        String stationName = "강남역";
        Station station = stations.save(new Station(stationName));

        assertThat(station.getName()).isEqualTo(stationName);
        assertThat(station.getId()).isNotNull();
    }

    @Test
    void findByName() {
        Station station = stations.findByName("테스트역1").get();

        assertThat(station.getName()).isEqualTo("테스트역1");
    }
}
