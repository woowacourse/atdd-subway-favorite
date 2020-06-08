package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.service.member.MemberServiceTest.*;

@DataJdbcTest
@Sql("/truncate.sql")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    public void findByEmail() {
        memberRepository.save(member);

        Member targetMember = memberRepository.findByEmail(TEST_USER_EMAIL)
                .orElse(null);
        assertThat(targetMember.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(targetMember.getName()).isEqualTo(TEST_USER_NAME);
        assertThat(targetMember.getPassword()).isEqualTo(TEST_USER_PASSWORD);
    }

    @Test
    public void existedByEmail() {
        memberRepository.save(member);

        assertThat(memberRepository.existsByEmail(TEST_USER_EMAIL))
                .isTrue();
    }

}