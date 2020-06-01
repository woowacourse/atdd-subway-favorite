package wooteco.subway.domain.favorite;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("select * from favorite where (id = :id) and (member_id = :memberId)")
    Optional<Favorite> findByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);

    @Query("select * from favorite where member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);
}
