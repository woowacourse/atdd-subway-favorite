package wooteco.subway;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.linestation.LineStation;
import wooteco.subway.domain.linestation.LineStations;

@SpringBootTest
@Sql("/data.sql")
public class SampleTest {

    @Autowired
    LineRepository lineRepository;

    @Test
    void sample() {
        List<Line> lines = lineRepository.findAll();
        LineStations stations = lines.get(0).getStations();
        List<LineStation> stations1 = stations.getStations();
        System.out.println(stations1.get(0));
    }
}
