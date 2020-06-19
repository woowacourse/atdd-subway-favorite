package wooteco.subway.domain.station;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByName(String name);

    @Query("select s from Station s where s.name in (:stations)")
    List<Station> findAllByStationName(@Param("stations") List<String> stations);
}
