package wooteco.subway.domain.station;

// import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// public interface StationRepository extends CrudRepository<Station, Long> {
//     @Override
//     List<Station> findAllById(Iterable ids);
//
//     @Override
//     List<Station> findAll();
//
//     @Query("select * from station where name = :stationName")
//     Optional<Station> findByName(@Param("stationName") String stationName);
// }

public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findAllById(Iterable ids);

    List<Station> findAll();

    Optional<Station> findByName(String name);
}