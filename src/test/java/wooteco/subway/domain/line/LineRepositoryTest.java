package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import wooteco.subway.domain.station.StationRepository;

@DataJpaTest
public class LineRepositoryTest {
    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

    private Line initialLine;

    @BeforeEach
    void setUp() {
        initialLine = lines.save(new Line("3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 10));
        initialLine.addLineStation(new LineStation(null, 1L, 10, 10));
        initialLine.addLineStation(new LineStation(1L, 2L, 10, 10));
    }

    @Test
    void find() {
        Line line = lines.findById(initialLine.getId()).get();

        // for 쿼리 확인
        lines.flush();

        assertThat(line.getName()).isEqualTo("3호선");
        assertThat(line.getStations().getStations()).hasSize(2);
    }

    @Test
    void addLineStation() {
        initialLine.addLineStation(new LineStation(2L, 3L, 10, 10));
        initialLine.addLineStation(new LineStation(3L, 4L, 10, 10));

        // for 쿼리 확인
        lines.flush();

        assertThat(initialLine.getStations().getStations()).hasSize(4);
    }
}
