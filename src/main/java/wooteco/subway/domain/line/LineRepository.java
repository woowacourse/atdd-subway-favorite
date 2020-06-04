package wooteco.subway.domain.line;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface LineRepository extends CrudRepository<Line, Long> {
    @Override
    List<Line> findAll();
}
