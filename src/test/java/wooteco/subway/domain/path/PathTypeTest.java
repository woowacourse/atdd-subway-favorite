package wooteco.subway.domain.path;

import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.LineStation;

import static org.assertj.core.api.Assertions.assertThat;

class PathTypeTest {

    @Test
    public void findWeightOf() {
        LineStation lineStation = new LineStation(1L, 2L, 10, 20);

        assertThat(PathType.DISTANCE.findWeightOf(lineStation)).isEqualTo(10);
        assertThat(PathType.DURATION.findWeightOf(lineStation)).isEqualTo(20);
    }
}