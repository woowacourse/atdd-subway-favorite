package wooteco.subway.domain.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(long memberId);

    List<Favorite> findAllByMemberName(String memberName);

    Optional<Favorite> findByMemberIdAndSourceIdAndTargetId(long memberId, long sourceId,
            long targetId);
}
