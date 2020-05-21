package wooteco.subway.domain.favorite;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("SELECT COUNT(*) FROM FAVORITE WHERE member_id = :memberId AND source = :source AND target = :target")
    Boolean hasFavorite(@Param("memberId") Long memberId, @Param("source") Long source,
        @Param("target") Long target);

    @Query("SELECT * FROM FAVORITE WHERE member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM FAVORITE WHERE member_id = :memberId AND id = :favoriteId")
    Boolean deleteByIdWithMemberId(@Param("favoriteId") Long favoriteId,
        @Param("memberId") Long memberId);
}
