package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    @Query("select * from favorite where member = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select * from favorite where source_station_id = :sourceStationId and target_station_id = :targetStationId")
    Optional<Favorite> findByIds(@Param("sourceStationId") Long sourceStationId, @Param("targetStationId") Long targetStationId);
}
