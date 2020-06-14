package wooteco.subway.service.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.LineStationService;

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

    @Test
    void findNameById() {
        Long id = 1L;
        String name = "잠실역";
        Station mockStation = mock(Station.class);
        when(mockStation.getId()).thenReturn(id);
        when(mockStation.getName()).thenReturn(name);

        when(stationRepository.findById(id)).thenReturn(Optional.of(mockStation));

        assertThat(stationService.findNameById(id)).isEqualTo(name);
    }
}
