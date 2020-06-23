package woowa.bossdog.subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woowa.bossdog.subway.domain.Station;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByName(String name);
}

