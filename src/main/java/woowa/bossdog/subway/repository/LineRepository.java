package woowa.bossdog.subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woowa.bossdog.subway.domain.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
}
