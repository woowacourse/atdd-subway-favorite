package wooteco.subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationsTest {
    @DisplayName("id와 이름으로 이루어진 Map을 잘 생성하는지 테스트")
    @Test
    void toNamesMapTest() {
        Station station1 = new Station(1L, "가");
        Station station2 = new Station(2L, "나");
        Station station3 = new Station(3L, "다");
        Station station4 = new Station(4L, "라");
        Station station5 = new Station(5L, "마");

        Stations stations = new Stations(Arrays.asList(station1, station2, station3, station4, station5));
        Map<Long, String> namesMap = stations.toNamesMap();
        assertThat(namesMap).hasSize(5);
        assertThat(namesMap.get(1L)).isEqualTo("가");
        assertThat(namesMap.get(2L)).isEqualTo("나");
        assertThat(namesMap.get(3L)).isEqualTo("다");
        assertThat(namesMap.get(4L)).isEqualTo("라");
        assertThat(namesMap.get(5L)).isEqualTo("마");
    }
}
