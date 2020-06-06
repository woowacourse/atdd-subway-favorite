package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    @Query("select * from favorite where member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from favorite where member_id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
