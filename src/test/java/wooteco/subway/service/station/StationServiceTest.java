package wooteco.subway.service.station;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
public class StationServiceTest {
    // @Autowired
    // private StationService stationService;
    //
    // @Autowired
    // private StationRepository stationRepository;
    // @Autowired
    // private LineRepository lineRepository;
    //
    // @Test
    // public void removeStation() {
    //     Station station1 = stationRepository.save(new Station("강남역"));
    //     Station station2 = stationRepository.save(new Station("역삼역"));
    //     Line line = lineRepository.save(new Line("2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 10));
    //
    //     line.addLineStation(new LineStation(null, station1.getId(), 10, 10));
    //     line.addLineStation(new LineStation(station1.getId(), station2.getId(), 10, 10));
    //     lineRepository.save(line);
    //
    //     stationService.deleteStationById(station1.getId());
    //
    //     Optional<Station> resultStation = stationRepository.findById(station1.getId());
    //     assertThat(resultStation).isEmpty();
    //
    //     Line resultLine = lineRepository.findById(line.getId()).orElseThrow(RuntimeException::new);
    //     assertThat(resultLine.getStations()).hasSize(1);
    // }
}
