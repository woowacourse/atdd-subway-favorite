package woowa.bossdog.subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import woowa.bossdog.subway.domain.Station;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @DisplayName("영속화 테스트")
    @Test
    void save_find() {
        // given
        final Station station = new Station("낙성대역");
        stationRepository.save(station);

        // when
        final Station findStation = stationRepository.findById(station.getId())
                .orElseThrow(NoSuchElementException::new);

        // then
        assertThat(findStation.getId()).isEqualTo(station.getId());
        assertThat(findStation.getName()).isEqualTo(station.getName());
    }

}