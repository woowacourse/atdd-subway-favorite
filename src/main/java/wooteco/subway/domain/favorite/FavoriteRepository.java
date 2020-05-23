package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("SELECT id, member_email, source, target FROM FAVORITE WHERE source = :source AND target = :target")
    Favorite findBySourceAndTarget(@Param("source") String source, @Param("target") String target);
}
