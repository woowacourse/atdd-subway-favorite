package wooteco.subway.domain.favorite;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("SELECT * FROM FAVORITE WHERE member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM FAVORITE WHERE member_id = :memberId AND source = :source AND target = :target")
    Boolean deleteByMemberIdAndSourceAndTarget(@Param("memberId") Long memberId,
        @Param("source") Long source, @Param("target") Long target);

    @Query("SELECT * FROM FAVORITE WHERE member_id = :memberId AND source = :source AND target = :target")
    Optional<Favorite> findBySourceAndTargetAndMember(@Param("memberId") Long memberId,
        @Param("source") Long source, @Param("target") Long target);

    @Modifying
    @Query("DELETE FROM FAVORITE WHERE member_id = :memberId AND id = :favoriteId")
    boolean deleteByIdWithMemberId(@Param("memberId") Long memberId,
        @Param("favoriteId") Long favoriteId);

    @Modifying
    @Query("DELETE FROM FAVORITE WHERE member_id = :memberId")
    void deleteByMemberIdWithAllFavorites(@Param("memberId") Long memberId);
}
