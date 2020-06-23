package woowa.bossdog.subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woowa.bossdog.subway.domain.LineStation;

import java.util.List;

public interface LineStationRepository extends JpaRepository<LineStation, Long> {
    List<LineStation> findAllByLineId(Long lineId);
}
