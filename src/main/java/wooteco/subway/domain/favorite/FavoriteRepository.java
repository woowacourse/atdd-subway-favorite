package wooteco.subway.domain.favorite;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<FavoriteStation, Long> {
    @Query("select * from favorite where member = :id")
    List<FavoriteStation> findByMemberId(@Param("id") Long id);
}
