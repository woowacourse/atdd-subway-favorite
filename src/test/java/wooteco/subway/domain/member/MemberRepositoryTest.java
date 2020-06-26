package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository members;

    @Test
    void find() {
        Member member = members.findByEmail("test@test.com").get();

        assertThat(member.getName()).isEqualTo("testUser");
    }
}
