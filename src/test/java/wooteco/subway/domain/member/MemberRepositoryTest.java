package wooteco.subway.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
public class MemberRepositoryTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Member persistMember = memberRepository.save(member);
        assertThat(persistMember.getId()).isNotNull();
    }

    @Test
    void findByEmail() {
        memberRepository.save(new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
        Member member = memberRepository.findByEmail(TEST_USER_EMAIL).orElseThrow(RuntimeException::new);
        assertThat(member).isNotNull();
    }
}
