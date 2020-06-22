package wooteco.subway.service.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineStationCreateRequest;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
	private static final String STATION_NAME1 = "강남역";
	private static final String STATION_NAME2 = "역삼역";
	private static final String STATION_NAME3 = "선릉역";
	private static final String STATION_NAME4 = "삼성역";

	@Mock
	private LineRepository lineRepository;
	@Mock
	private LineStationService lineStationService;
	@Mock
	private StationRepository stationRepository;

	private LineService lineService;

	private Line line;
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;

	@BeforeEach
	void setUp() {
		lineService = new LineService(lineStationService, lineRepository, stationRepository);

		station1 = Station.of(1L, STATION_NAME1);
		station2 = Station.of(2L, STATION_NAME2);
		station3 = Station.of(3L, STATION_NAME3);
		station4 = Station.of(4L, STATION_NAME4);

		line = Line.of(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		line.addLineStation(LineStation.of(null, station1, 10, 10));
		line.addLineStation(LineStation.of(station1, station2, 10, 10));
		line.addLineStation(LineStation.of(station2, station3, 10, 10));
	}

	@Test
	void addLineStationAtTheFirstOfLine() {
		LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10, 10);

		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		when(stationRepository.findById(station4.getId())).thenReturn(Optional.of(station4));

		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Station> stationIds = line.getSortedStations();
		assertThat(stationIds.get(0).getId()).isEqualTo(4L);
		assertThat(stationIds.get(1).getId()).isEqualTo(1L);
		assertThat(stationIds.get(2).getId()).isEqualTo(2L);
		assertThat(stationIds.get(3).getId()).isEqualTo(3L);
	}

	@Test
	void addLineStationBetweenTwo() {
		LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(), station4.getId(), 10, 10);

		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		when(stationRepository.findById(station1.getId())).thenReturn(Optional.of(station1));
		when(stationRepository.findById(station4.getId())).thenReturn(Optional.of(station4));

		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Station> stationIds = line.getSortedStations();
		assertThat(stationIds.get(0).getId()).isEqualTo(1L);
		assertThat(stationIds.get(1).getId()).isEqualTo(4L);
		assertThat(stationIds.get(2).getId()).isEqualTo(2L);
		assertThat(stationIds.get(3).getId()).isEqualTo(3L);
	}

	@Test
	void addLineStationAtTheEndOfLine() {
		LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(), station4.getId(), 10, 10);

		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		when(stationRepository.findById(station3.getId())).thenReturn(Optional.of(station3));
		when(stationRepository.findById(station4.getId())).thenReturn(Optional.of(station4));


		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Station> stationIds = line.getSortedStations();
		assertThat(stationIds.get(0).getId()).isEqualTo(1L);
		assertThat(stationIds.get(1).getId()).isEqualTo(2L);
		assertThat(stationIds.get(2).getId()).isEqualTo(3L);
		assertThat(stationIds.get(3).getId()).isEqualTo(4L);
	}

	@Test
	void removeLineStationAtTheFirstOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 1L);

		assertThat(line.getStations()).hasSize(2);

		List<Station> stationIds = line.getSortedStations();
		assertThat(stationIds.get(0).getId()).isEqualTo(2L);
		assertThat(stationIds.get(1).getId()).isEqualTo(3L);
	}

	@Test
	void removeLineStationBetweenTwo() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 2L);

		assertThat(line.getStations()).hasSize(2);
	}

	@Test
	void removeLineStationAtTheEndOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 3L);

		assertThat(line.getStations()).hasSize(2);

		List<Station> stationIds = line.getSortedStations();
		assertThat(stationIds.get(0).getId()).isEqualTo(1L);
		assertThat(stationIds.get(1).getId()).isEqualTo(2L);
	}
}
