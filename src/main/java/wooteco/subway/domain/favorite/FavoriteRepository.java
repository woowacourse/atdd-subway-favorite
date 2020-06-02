package wooteco.subway.domain.favorite;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("SELECT * FROM favorite WHERE member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT * FROM favorite " +
            "WHERE member_id = :memberId AND departure_id = :departureId AND arrival_id = :arrivalId")
    Optional<Favorite> findByMemberIdAndDepartureIdAndArrivalId(@Param("memberId") Long memberId,
                                                            @Param("departureId") Long departureId,
                                                            @Param("arrivalId") Long arrivalId);
}
