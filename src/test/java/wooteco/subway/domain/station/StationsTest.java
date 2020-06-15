package wooteco.subway.domain.station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StationsTest {

    private Stations stations;

    @BeforeEach
    void setUp() {
        List<Station> station = Arrays.asList(new Station(1L, "강남역"), new Station(2L, "역삼역"));
        stations = Stations.of(station);
    }

    @Test
    void extractStationById() {
        assertThat(stations.extractStationById(1L).getId()).isEqualTo(1L);
        assertThat(stations.extractStationById(1L).getName()).isEqualTo("강남역");
    }
}