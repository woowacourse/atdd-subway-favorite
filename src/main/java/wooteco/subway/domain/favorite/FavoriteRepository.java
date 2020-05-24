package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("select * from favorite where member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);
}
