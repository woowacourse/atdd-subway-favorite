package wooteco.subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJdbcTest
public class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;

	@DisplayName("이미 저장된 역 다시 저장시 예외처리")
	@Test
	void saveStation() {
		String stationName = "강남역";
		stationRepository.save(new Station(stationName));

		assertThrows(DbActionExecutionException.class, () -> stationRepository.save(new Station(stationName)));
	}
}
