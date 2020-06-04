package wooteco.subway.service.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.dto.LineStationCreateRequest;

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

	private LineService lineService;

	private Line line;
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;

	@BeforeEach
	void setUp() {
		lineService = new LineService(lineStationService, lineRepository);

		station1 = Station.of(STATION_NAME1).withId(1L);
		station2 = Station.of(STATION_NAME2).withId(2L);
		station3 = Station.of(STATION_NAME3).withId(3L);
		station4 = Station.of(STATION_NAME4).withId(4L);

		line = Line.of("2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5).withId(1L);
		line.addLineStation(LineStation.of(null, 1L, 10, 10).withId(1L));
		line.addLineStation(LineStation.of(1L, 2L, 10, 10).withId(2L));
		line.addLineStation(LineStation.of(2L, 3L, 10, 10).withId(3L));
	}

	@Test
	void addLineStationAtTheFirstOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

		LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10,
			10);
		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Long> stationIds = line.getStationIds();
		assertThat(stationIds.get(0)).isEqualTo(4L);
		assertThat(stationIds.get(1)).isEqualTo(1L);
		assertThat(stationIds.get(2)).isEqualTo(2L);
		assertThat(stationIds.get(3)).isEqualTo(3L);
	}

	@Test
	void addLineStationBetweenTwo() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

		LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(),
			station4.getId(), 10, 10);
		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Long> stationIds = line.getStationIds();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(4L);
		assertThat(stationIds.get(2)).isEqualTo(2L);
		assertThat(stationIds.get(3)).isEqualTo(3L);
	}

	@Test
	void addLineStationAtTheEndOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

		LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(),
			station4.getId(), 10, 10);
		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Long> stationIds = line.getStationIds();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
		assertThat(stationIds.get(2)).isEqualTo(3L);
		assertThat(stationIds.get(3)).isEqualTo(4L);
	}

	@Test
	void removeLineStationAtTheFirstOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 1L);

		assertThat(line.getStations()).hasSize(2);

		List<Long> stationIds = line.getStationIds();
		assertThat(stationIds.get(0)).isEqualTo(2L);
		assertThat(stationIds.get(1)).isEqualTo(3L);
	}

	@Test
	void removeLineStationBetweenTwo() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 2L);

		verify(lineRepository).save(any());
	}

	@Test
	void removeLineStationAtTheEndOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 3L);

		assertThat(line.getStations()).hasSize(2);

		List<Long> stationIds = line.getStationIds();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
	}
}
