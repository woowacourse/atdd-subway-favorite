package wooteco.subway.domain.line;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LineRepository extends CrudRepository<Line, Long> {
    @Override
    List<Line> findAll();
}
