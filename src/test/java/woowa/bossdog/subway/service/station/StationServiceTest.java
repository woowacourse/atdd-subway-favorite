package woowa.bossdog.subway.service.station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.repository.StationRepository;
import woowa.bossdog.subway.service.station.dto.StationRequest;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@SpringBootTest
class StationServiceTest {

    private StationService stationService;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        stationService = new StationService(stationRepository);
    }

    @DisplayName("지하철 역 추가")
    @Test
    void createStation() {
        // given
        final Station station = new Station("강남역");
        given(stationRepository.save(any())).willReturn(station);

        // when
        final StationResponse response = stationService.createStation(new StationRequest("강남역"));

        // then
        verify(stationRepository).save(any());
        assertThat(response.getId()).isEqualTo(station.getId());
        assertThat(response.getName()).isEqualTo(station.getName());
    }

    @DisplayName("지하철 역 목록 조회")
    @Test
    void listStation() {
        // given
        final List<Station> stations = new ArrayList<>();
        stations.add(new Station("강남역"));
        stations.add(new Station("선릉역"));
        given(stationRepository.findAll()).willReturn(stations);

        // when
        final List<StationResponse> responses = stationService.listStations();

        // then
        verify(stationRepository).findAll();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("강남역");
        assertThat(responses.get(1).getName()).isEqualTo("선릉역");
    }

    @DisplayName("지하철 역 단건 조회")
    @Test
    void findStation() {
        // given
        final Station station = new Station(63L, "선릉역");
        given(stationRepository.findById(any())).willReturn(Optional.of(station));

        // when
        final StationResponse response = stationService.findStation(station.getId());

        // then
        verify(stationRepository).findById(eq(63L));
        assertThat(response.getId()).isEqualTo(station.getId());
        assertThat(response.getName()).isEqualTo(station.getName());
    }

    @DisplayName("지하철 역 삭제")
    @Test
    void removeStation() {
        // given
        final Station station = new Station(63L, "선릉역");

        // when
        stationService.deleteStation(station.getId());

        // then
        verify(stationRepository).deleteById(eq(63L));
    }
}