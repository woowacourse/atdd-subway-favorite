package wooteco.subway.domain.favorite;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.subway.domain.station.Station;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("select * from favorite where member_id = :member_id")
    List<Favorite> findAllByMemberId(@Param("member_id") Long memberId);
}
