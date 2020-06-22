package woowa.bossdog.subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woowa.bossdog.subway.domain.Favorite;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMemberId(Long memberId);
}
