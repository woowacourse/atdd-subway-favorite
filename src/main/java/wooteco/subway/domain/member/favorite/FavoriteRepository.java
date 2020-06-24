package wooteco.subway.domain.member.favorite;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    void deleteAllBySourceStationId(Long id);

    void deleteAllByTargetStationId(Long id);

    Optional<Favorite> findByMemberIdAndSourceStationIdAndTargetStationId(Long memberId,
        Long sourceStationId, Long targetStationId);
}
