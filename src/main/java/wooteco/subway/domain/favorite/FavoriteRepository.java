package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("select * from favorite where member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from favorite where member_id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Query("select count(*) > 0 from favorite where member_id = :memberId and " +
            "source_station_id = :sourceId and target_station_id = :targetId")
    boolean existsBy(@Param("memberId") Long memberId,
                     @Param("sourceId") Long sourceId,
                     @Param("targetId") Long targetId);
}
