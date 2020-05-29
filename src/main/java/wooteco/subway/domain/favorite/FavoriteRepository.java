package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("SELECT * FROM favorite WHERE source = :source AND target = :target")
    Optional<Favorite> findBySourceAndTarget(@Param("source") String source, @Param("target") String target);

    @Query("SELECT * FROM favorite WHERE member_email = :email")
    List<Favorite> findByEmail(@Param("email") String email);

    @Query("SELECT * FROM favorite WHERE member_id = :member_id")
    List<Favorite> findAllByMemberId(@Param("member_id") Long memberId);
}
