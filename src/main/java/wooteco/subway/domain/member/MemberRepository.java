package wooteco.subway.domain.member;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends CrudRepository<Member, Long> {
    @Query("SELECT * FROM member WHERE email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM favorite WHERE id = :id")
    void deleteFavoriteById(@Param("id") Long favoriteId);
}
