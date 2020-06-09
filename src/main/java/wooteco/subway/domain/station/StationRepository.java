package wooteco.subway.domain.station;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StationRepository extends CrudRepository<Station, Long> {
    @Override
    List<Station> findAllById(Iterable ids);

    @Override
    List<Station> findAll();

    @Query("select id from station where name IN (:names)")
    List<Long> findIdsByNames(@Param("names") List<String> names);
}
