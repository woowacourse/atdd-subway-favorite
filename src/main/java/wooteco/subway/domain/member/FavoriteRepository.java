package wooteco.subway.domain.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select f from Favorite f "
            + "join fetch f.member join fetch f.source join fetch f.target")
        // @EntityGraph(attributePaths = {"member", "source", "target"})
    List<Favorite> findAll();

    @Query("select f from Favorite f "
            + "join fetch f.member join fetch f.source join fetch f.target "
            + "where f.member.id = :memberId")
    List<Favorite> findAllByMemberId(long memberId);

    @Query("select f from Favorite f "
            + "join fetch f.member join fetch f.source join fetch f.target "
            + "where f.member.name = :memberName")
    List<Favorite> findAllByMemberName(String memberName);

    @Query("select f from Favorite f "
            + "join fetch f.member join fetch f.source join fetch f.target "
            + "where f.member.id = :memberId "
            + "and f.source.id = :sourceId "
            + "and f.target.id = :targetId")
    Optional<Favorite> findByAllInfo(long memberId, long sourceId, long targetId);
}
