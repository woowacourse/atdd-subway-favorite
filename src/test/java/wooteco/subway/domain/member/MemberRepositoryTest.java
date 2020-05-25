package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_NAME;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_PASSWORD;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
public class MemberRepositoryTest {
    private static final Member MEMBER_BROWN = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void addFavorite() {
        Member persistMember = memberRepository.save(MEMBER_BROWN);

        persistMember.addFavorite(new Favorite(1L, 2L));

        Member resultMember = memberRepository.save(persistMember);
        assertThat(resultMember.getFavorites().size()).isEqualTo(1);
    }
}
