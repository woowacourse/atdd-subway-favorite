package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_NAME;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
public class MemberRepositoryTest {
    private Member MEMBER_BROWN;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MEMBER_BROWN = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        MEMBER_BROWN.addFavorite(new Favorite(2L, 3L));
        MEMBER_BROWN.addFavorite(new Favorite(3L, 4L));
    }

    @Test
    void addFavorite() {
        Member persistMember = memberRepository.save(MEMBER_BROWN);

        persistMember.addFavorite(new Favorite(1L, 2L));

        Member resultMember = memberRepository.save(persistMember);
        assertThat(resultMember.getFavorites().size()).isEqualTo(3);
    }

    @Test
    void deleteFavorite() {
        MEMBER_BROWN.deleteFavorite(new Favorite(2L, 3L));

        Member resultMember = memberRepository.save(MEMBER_BROWN);
        assertThat(resultMember.getFavorites()).hasSize(1);
    }
}
