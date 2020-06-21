package woowa.bossdog.subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import woowa.bossdog.subway.domain.Station;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    @Query("select s from Station s where s.name = :name")
    Optional<Station> findByName(@Param("name") String name);
}

