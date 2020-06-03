package wooteco.subway.domain.favorite;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    @Query("SELECT * FROM favorite WHERE member_id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT * FROM favorite WHERE member_id = :memberId AND departure_station_id = :departureStationId AND arrival_station_id = :arrivalStationId")
    Optional<Favorite> findByMemberIdAndDepartureStationIdAndArrivalStationId(
        @Param("memberId") Long memberId,
        @Param("departureStationId") Long departureStationId,
        @Param("arrivalStationId") Long arrivalStationId);

    @Query("DELETE FROM favorite WHERE member_id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Query("DELETE FROM favorite WHERE departure_station_id = :stationId OR arrival_station_id = :stationId")
    void deleteAllByStationId(@Param("stationId") Long stationId);
}
