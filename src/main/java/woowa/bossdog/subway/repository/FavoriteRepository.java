package woowa.bossdog.subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import woowa.bossdog.subway.domain.Favorite;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select f from Favorite f where f.member.id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

//    @Query("delete from Favorite f where f.member.id = :memberId")
//    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
