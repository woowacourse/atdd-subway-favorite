package wooteco.subway.domain.member;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {
    @Query("select * from member where email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("select count(*) > 0 from member where email = :email")
    boolean existsByEmail(@Param("email") String email);
}
