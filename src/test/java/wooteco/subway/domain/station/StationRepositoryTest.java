package wooteco.subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @DisplayName("역 id 목록으로 역을 잘 가져오는지 테스트")
    @Test
    void findAllByIdTest() {
        stationRepository.save(new Station("가"));
        stationRepository.save(new Station("나"));
        stationRepository.save(new Station("다"));
        stationRepository.save(new Station("라"));

        List<Station> stations = stationRepository.findAllById(Arrays.asList(1L, 2L, 2L));
        assertThat(stations).hasSize(2);
        assertThat(stations.get(0).getId()).isEqualTo(1L);
        assertThat(stations.get(1).getId()).isEqualTo(2L);
    }
}
