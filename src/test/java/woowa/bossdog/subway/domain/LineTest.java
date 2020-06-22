package woowa.bossdog.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.bossdog.subway.service.line.dto.UpdateLineRequest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @DisplayName("지하철 노선 정보 수정")
    @Test
    void update() {
        // given
        Line line = new Line("2호선", LocalTime.of(5,30), LocalTime.of(23,30), 10);
        UpdateLineRequest request = new UpdateLineRequest("신분당선", LocalTime.of(4,30), LocalTime.of(22,30), 15);

        // when
        line.update(request);

        // then
        assertThat(line.getName()).isEqualTo(request.getName());
        assertThat(line.getStartTime()).isEqualTo(request.getStartTime());
        assertThat(line.getEndTime()).isEqualTo(request.getEndTime());
        assertThat(line.getIntervalTime()).isEqualTo(request.getIntervalTime());
    }

    @DisplayName("노선에 포함된 구간 역 조회")
    @Test
    void getStationIds() {
        // given
        Line line = new Line("2호선", LocalTime.of(5,30), LocalTime.of(23,30), 10);
        final LineStation lineStation1 = new LineStation(line, new Station(null), new Station(1L, "강남역"), 10, 10);
        final LineStation lineStation2 = new LineStation(line, new Station(3L, "역삼역"), new Station(6L, "잠실역"), 10, 10);
        final LineStation lineStation3 = new LineStation(line, new Station(1L, "강남역"), new Station(3L, "역삼역"), 10, 10);

        line.getLineStations().add(lineStation1);
        line.getLineStations().add(lineStation2);
        line.getLineStations().add(lineStation3);

        // when
        final List<Station> stations = line.getStations();

        // then
        assertThat(stations.get(0).getId()).isEqualTo(1L);
        assertThat(stations.get(1).getId()).isEqualTo(3L);
        assertThat(stations.get(2).getId()).isEqualTo(6L);
    }

}