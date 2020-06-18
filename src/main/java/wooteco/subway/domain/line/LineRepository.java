package wooteco.subway.domain.line;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Override
    List<Line> findAll();

    @Query("select l from Line l join fetch l.lineStations ls on l.id = :id")
    Line findLineJoinFetch(@Param("id") Long id);
}
