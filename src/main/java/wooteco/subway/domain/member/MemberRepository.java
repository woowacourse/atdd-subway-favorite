package wooteco.subway.domain.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // @Query("SELECT * FROM member WHERE email = :email")
    Optional<Member> findByEmail(String email);

    // @Modifying
    // @Query("DELETE FROM favorite WHERE id = :id")
    void deleteById(Long favoriteId);
}
