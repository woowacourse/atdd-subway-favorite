package wooteco.subway.domain.station;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAllById(Iterable ids);

    @Override
    List<Station> findAll();

    // @Query("SELECT * FROM station WHERE name = :stationName")
    Optional<Station> findByName(String stationName);
}
