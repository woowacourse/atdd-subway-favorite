package wooteco.subway.domain.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.subway.domain.favorite.FavoriteDetail;

public interface MemberRepository extends CrudRepository<Member, Long> {
    @Query("select * from member where email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query(
        "select source.id as source_id, target.id as target_id, source.name as source_name, target.name as target_name from favorite "
            + "INNER JOIN station as source "
            + "ON favorite.source_station_id = source.id "
            + "INNER JOIN station as target "
            + "ON favorite.target_station_id = target.id "
            + "WHERE favorite.member = :id")
    List<FavoriteDetail> findFavoritesById(@Param("id") Long id);
}
