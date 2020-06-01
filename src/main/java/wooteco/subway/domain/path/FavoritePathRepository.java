package wooteco.subway.domain.path;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoritePathRepository extends CrudRepository<FavoritePath, Long> {
    @Query("SELECT * FROM favorite_path WHERE member_id=:member_id")
    List<FavoritePath> findAllByMemberId(@Param("member_id") Long memberId);

    @Query("SELECT * FROM favorite_path WHERE member_id=:member_id AND id=:path_id")
    Optional<FavoritePath> findByMemberIdAndPathId(@Param("member_id") Long memberId, @Param("path_id") Long pathId);

    @Query("SELECT * FROM favorite_path WHERE member_id=:member_id AND source_id=:source_id AND target_id=:target_id")
    Optional<FavoritePath> findByUniqueField(@Param("member_id") Long memberId, @Param("source_id") Long sourceId,
        @Param("target_id") Long targetId);
}
