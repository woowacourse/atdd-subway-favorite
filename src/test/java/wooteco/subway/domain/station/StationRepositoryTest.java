package wooteco.subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJdbcTest
@Sql("/truncate.sql")
public class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;

    @DisplayName("지하철역 저장")
    @Test
    void saveStation() {
        String stationName = "강남역";
        stationRepository.save(new Station(stationName));

        assertThrows(DbActionExecutionException.class, () -> stationRepository.save(new Station(stationName)));
    }
}
