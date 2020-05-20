package wooteco.subway.service.line;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
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
import wooteco.subway.service.line.dto.LineDetailResponse;

@ExtendWith(MockitoExtension.class)
public class LineStationServiceTest {
	private static final String STATION_NAME1 = "강남역";
	private static final String STATION_NAME2 = "역삼역";
	private static final String STATION_NAME3 = "선릉역";
	private static final String STATION_NAME4 = "삼성역";

	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;

	private LineStationService lineStationService;

	private Line line;
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;

	@BeforeEach
	void setUp() {
		lineStationService = new LineStationService(lineRepository, stationRepository);

		station1 = Station.of(STATION_NAME1).withId(1L);
		station2 = Station.of(STATION_NAME2).withId(2L);
		station3 = Station.of(STATION_NAME3).withId(3L);
		station4 = Station.of(STATION_NAME4).withId(4L);

		line = Line.of("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5).withId(1L);
		line.addLineStation(new LineStation(null, 1L, 10, 10));
		line.addLineStation(new LineStation(1L, 2L, 10, 10));
		line.addLineStation(new LineStation(2L, 3L, 10, 10));
	}

	@Test
	void findLineWithStationsById() {
		List<Station> stations = Lists.newArrayList(station1, station2, station3);
		when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));
		when(stationRepository.findAllById(anyList())).thenReturn(stations);

		LineDetailResponse lineDetailResponse = lineStationService.findLineWithStationsById(1L);

		assertThat(lineDetailResponse.getStations()).hasSize(3);
	}

	@Test
	void wholeLines() {
		Line newLine = Line.of("신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5).withId(2L);
		newLine.addLineStation(new LineStation(null, 4L, 10, 10));
		newLine.addLineStation(new LineStation(4L, 5L, 10, 10));
		newLine.addLineStation(new LineStation(5L, 6L, 10, 10));

		List<Station> stations = Lists.newArrayList(Station.of("강남역").withId(1L), Station.of("역삼역").withId(2L),
			Station.of("삼성역").withId(3L), Station.of("양재역").withId(4L), Station.of("양재시민의숲역").withId(5L),
			Station.of("청계산입구역").withId(6L));

		when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line, newLine));
		when(stationRepository.findAllById(anyList())).thenReturn(stations);

		List<LineDetailResponse> lineDetails = lineStationService.findLinesWithStations().getLineDetailResponse();

		assertThat(lineDetails).isNotNull();
		assertThat(lineDetails.get(0).getStations().size()).isEqualTo(3);
		assertThat(lineDetails.get(1).getStations().size()).isEqualTo(3);
	}
}
