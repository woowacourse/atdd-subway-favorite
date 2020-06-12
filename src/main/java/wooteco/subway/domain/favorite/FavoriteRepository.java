package wooteco.subway.domain.favorite;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    @Query("SELECT count(*) FROM FAVORITE WHERE member_id = :memberId AND source_station_id = :sourceId AND target_station_id = :targetId")
    boolean hasFavorite(@Param("memberId") Long memberId, @Param("sourceId") Long sourceId,
        @Param("targetId") Long targetId);

    @Query("SELECT * FROM FAVORITE WHERE member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM FAVORITE WHERE member_id = :memberId AND id = :favoriteId")
    boolean deleteByIdWithMemberId(@Param("memberId") Long memberId,
        @Param("favoriteId") Long favoriteId);
}
