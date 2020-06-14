package wooteco.subway.service.station;

import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.LineStationService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class StationServiceTest {

    private StationRepository stationRepository = mock(StationRepository.class);
    private LineStationService lineStationService = mock(LineStationService.class);
    private StationService stationService = new StationService(lineStationService, stationRepository);

    @Test
    public void removeStation() {
        final Long id = 1L;
        stationService.deleteStationById(id);

        verify(lineStationService).deleteLineStationByStationId(id);
        verify(stationRepository).deleteById(id);
    }

    @Test
    void findByName() {
        String stationName = "강남역";
        Station expected = new Station(stationName);
        when(stationRepository.findByName(stationName)).thenReturn(Optional.of(expected));
        Station station = stationService.findByName(stationName);
        assertThat(station).isEqualTo(expected);
    }
}
