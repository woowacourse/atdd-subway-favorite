package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("SELECT * FROM favorite WHERE source = :source AND target = :target")
    Optional<Favorite> findBySourceAndTarget(@Param("source") String source, @Param("target") String target);
}
