package wooteco.subway.domain.line;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LineStationTest {
    @Test
    public void updatePreLineStationId() {
        LineStation lineStation = new LineStation(2L, 3L, 10, 10);

        lineStation.updatePreLineStation(6L);
        assertThat(lineStation.getPreStationId()).isEqualTo(6L);
    }

    @Test
    public void isLineStationOf() {
        LineStation lineStation = new LineStation(2L, 3L, 10, 10);

        assertThat(lineStation.isLineStationOf(2L, 3L)).isTrue();
    }

}