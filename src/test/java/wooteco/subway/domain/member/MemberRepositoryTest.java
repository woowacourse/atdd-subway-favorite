package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

@DataJdbcTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        memberRepository.save(member);
    }

    @Test
    void findByEmail() {
        Member findMember = memberRepository.findByEmail(this.member.getEmail()).get();
        assertThat(findMember).isEqualToComparingFieldByField(member);
    }

    @Test
    void duplicatedEmail() {
        Member newMember = new Member(TEST_USER_EMAIL, "KYLE", "ORANGE");
        memberRepository.save(member);

        assertThatThrownBy(() -> {
            memberRepository.save(newMember);
        }).isInstanceOf(DbActionExecutionException.class);
    }
}
