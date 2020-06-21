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
        final LineStation lineStation1 = new LineStation(null, 1L, 10, 10);
        final LineStation lineStation2 = new LineStation(3L, 6L, 10, 10);
        final LineStation lineStation3 = new LineStation(1L, 3L, 10, 10);

        line.getLineStations().add(lineStation1);
        line.getLineStations().add(lineStation2);
        line.getLineStations().add(lineStation3);

        // when
        final List<Long> stationIds = line.getStationIds();

        // then
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
        assertThat(stationIds.get(2)).isEqualTo(6L);
    }

}