package wooteco.subway.service.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

@SpringBootTest
@Sql("/truncate.sql")
public class StationServiceTest {
    @Autowired
    private StationService stationService;

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @DisplayName("역 제거시 노선의 역도 함께 제거")
    @Test
    public void removeStation() {
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));
        Line line = lineRepository.save(new Line("2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 10, "bg-black-500"));

        line.addLineStation(new LineStation(null, 강남역.getId(), 10, 10));
        line.addLineStation(new LineStation(강남역.getId(), 역삼역.getId(), 10, 10));
        lineRepository.save(line);

        stationService.deleteStationById(강남역.getId());

        Optional<Station> resultStation = stationRepository.findById(강남역.getId());
        assertThat(resultStation).isEmpty();

        Line resultLine = lineRepository.findById(line.getId()).orElseThrow(RuntimeException::new);
        assertThat(resultLine.getStations()).hasSize(1);
    }
}
