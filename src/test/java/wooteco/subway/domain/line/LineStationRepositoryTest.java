package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

@DataJpaTest
class LineStationRepositoryTest {
    @Autowired
    private LineStationRepository lineStationRepository;
    @Autowired
    private StationRepository stationRepository;

    @Test
    @Transactional
    void addLineStation() {
        // given
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Station station1 = stationRepository.save(new Station(1L, "잠실역"));
        Station station2 = stationRepository.save(new Station(2L, "선릉역"));
        lineStationRepository.save(new LineStation(line, null, station1, 10, 10));
        lineStationRepository.save(new LineStation(line, station1, station2, 10, 10));

        // then
        assertAll(
            () -> assertThat(lineStationRepository.findById(1L).get().getPreStation()).isNull(),
            () -> assertThat(
                lineStationRepository.findById(2L).get().getPreStation().getId()).isEqualTo(1L)
        );
    }
}