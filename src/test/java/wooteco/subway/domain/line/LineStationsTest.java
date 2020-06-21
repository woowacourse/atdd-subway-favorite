package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.subway.domain.station.Station;

class LineStationsTest {
    private Station 강남역;
    private Station 교대역;
    private Station 사당역;

    private LineStation null_강남역;
    private LineStation 강남역_교대역;
    private LineStation 교대역_사당역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        사당역 = new Station("사당역");

        null_강남역 = new LineStation(null, 강남역, 1, 1);
        강남역_교대역 = new LineStation(강남역, 교대역, 1, 1);
        교대역_사당역 = new LineStation(교대역, 사당역, 1, 1);
    }

    @Test
    void add() {
        LineStations lineStations = LineStations.empty();
        lineStations.add(null_강남역);
        assertThat(lineStations.getStations()).containsExactlyInAnyOrder(null_강남역);

        lineStations.add(강남역_교대역);
        assertThat(lineStations.getStations()).containsExactlyInAnyOrder(null_강남역, 강남역_교대역);
    }

    @Test
    void add2() {
        LineStation 강남역_사당역 = new LineStation(강남역, 사당역, 1, 1);
        LineStations lineStations = new LineStations(
            new HashSet<>(Arrays.asList(null_강남역, 강남역_사당역)));

        lineStations.add(강남역_교대역);

        assertThat(lineStations.getStations()).containsExactlyInAnyOrder(null_강남역, 강남역_교대역, 교대역_사당역);

        Station 역삼역 = new Station("역삼역");
        LineStation null_역삼역 = new LineStation(null, 역삼역, 1, 1);
        lineStations.add(null_역삼역);

        LineStation 역삼역_강남역 = new LineStation(역삼역, 강남역, 1, 1);
        assertThat(lineStations.getStations()).containsExactlyInAnyOrder(null_역삼역, 역삼역_강남역, 강남역_교대역, 교대역_사당역);
    }

    @Test
    void removeLineStation() {
        LineStations lineStations = new LineStations(new HashSet<>(Arrays.asList(null_강남역, 강남역_교대역, 교대역_사당역)));
        lineStations.remove(강남역_교대역);
        assertThat(lineStations.getStations()).containsExactlyInAnyOrder(null_강남역, new LineStation(강남역, 사당역, 1,1));
    }
}
