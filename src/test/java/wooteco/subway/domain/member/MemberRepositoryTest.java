package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.jdbc.Sql;

@DataJdbcTest
@Sql("/truncate.sql")
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("주어진 이메일을 가진 회원이 존재하는지 조회한다.")
    @ParameterizedTest
    @CsvSource({"existEmail@email.com, true", "nonExistEmail@email.com, false"})
    void existsByEmail(String email, boolean expected) {
        Member member = new Member("existEmail@email.com", "sample", "12345!");
        memberRepository.save(member);
        boolean actual = memberRepository.existsByEmail(email);
        assertThat(actual).isEqualTo(expected);
    }
}