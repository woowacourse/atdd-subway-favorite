package wooteco.subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.service.station.NotExistedStationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("지하철역 조회")
    @Test
    void findAllById() {
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 선릉역 = stationRepository.save(new Station("선릉역"));
        Station 삼성역 = stationRepository.save(new Station("삼성역"));
        Station 잠실역 = stationRepository.save(new Station("잠실역"));

        List<Long> stationIds = new ArrayList<>(Arrays.asList(선릉역.getId(), 삼성역.getId()));
        final List<Station> stations = stationRepository.findAllById(stationIds);
        assertThat(stations).hasSize(2);
        assertThat(stations.get(0).getName()).isEqualTo("선릉역");
        assertThat(stations.get(1).getName()).isEqualTo("삼성역");
    }

    @DisplayName("모든 지하철역 조회")
    @Test
    public void findAll() {
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 선릉역 = stationRepository.save(new Station("선릉역"));
        Station 삼성역 = stationRepository.save(new Station("삼성역"));
        Station 잠실역 = stationRepository.save(new Station("잠실역"));

        final List<Station> stations = stationRepository.findAll();
        assertThat(stations).hasSize(4);
    }

    @Test
    public void findByName() {
        stationRepository.save(new Station("강남역"));

        final Station station = stationRepository.findByName("강남역")
                .orElseThrow(NotExistedStationException::new);

        assertThat(station).isNotNull();
        assertThat(station.getName()).isEqualTo("강남역");
    }

    @Test
    public void findNameById() {
        Station 강남역 = stationRepository.save(new Station("강남역"));

        final String name = stationRepository.findNameById(강남역.getId());

        assertThat(name).isNotNull();
        assertThat(name).isEqualTo("강남역");
    }

//
//    @Override
//    List<Station> findAllById(Iterable ids);
//
//    @Override
//    List<Station> findAll();
//
//    @Query("select * from station where name = :stationName")
//    Optional<Station> findByName(@Param("stationName") String stationName);
//
//    @Query("select name from station where id = :stationId")
//    String findNameById(@Param("stationId") Long stationId);
}
