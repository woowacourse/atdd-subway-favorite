package wooteco.subway.domain.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LinesTest {

    private Lines lines;

    @BeforeEach
    void setUp() {
        Line line1 = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 10, 10));

        Line line2 = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(1L, 2L, 10, 10));

        Line line3 = new Line(3L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line3.addLineStation(new LineStation(2L, 3L, 10, 10));

        lines = new Lines(Arrays.asList(line1, line2, line3));
    }

    @Test
    void getStationIds() {
        assertThat(lines.getStationIds()).hasSize(3);
    }
}