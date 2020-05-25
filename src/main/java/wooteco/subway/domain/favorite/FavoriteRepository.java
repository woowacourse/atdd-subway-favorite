package wooteco.subway.domain.favorite;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("SELECT * FROM favorite WHERE member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT * FROM favorite WHERE member_id = :memberId AND departure = :departure AND arrival = :arrival")
    Optional<Favorite> findByMemberIdAndDepartureAndArrival(@Param("memberId") Long memberId,
                                                            @Param("departure") String departure,
                                                            @Param("arrival") String arrival);
}
