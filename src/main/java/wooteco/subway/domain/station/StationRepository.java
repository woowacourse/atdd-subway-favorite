package wooteco.subway.domain.station;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends CrudRepository<Station, Long> {
    @Override
    List<Station> findAllById(Iterable ids);

    @Override
    List<Station> findAll();

    @Query("select * from station where name = :stationName")
    Optional<Station> findByName(@Param("stationName") String stationName);

    @Query("select name from station where id = :stationId")
    String findNameById(@Param("stationId") Long stationId);
}
