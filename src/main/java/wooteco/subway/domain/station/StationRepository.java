package wooteco.subway.domain.station;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StationRepository extends CrudRepository<Station, Long> {
    @Override
    List<Station> findAllById(Iterable ids);

    @Override
    List<Station> findAll();

    Optional<Station> findByName(@Param("stationName") String stationName);
}
