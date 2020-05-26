package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.favorite.Favorite;

class MemberTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    void update() {
        member.update(TEST_OTHER_USER_NAME, TEST_OTHER_USER_PASSWORD);
        assertThat(member.getName()).isEqualTo(TEST_OTHER_USER_NAME);
        assertThat(member.getPassword()).isEqualTo(TEST_OTHER_USER_PASSWORD);
    }

    @Test
    void checkPassword() {
        assertThat(member.checkPassword(TEST_USER_PASSWORD)).isTrue();
        assertThat(member.checkPassword(TEST_OTHER_USER_PASSWORD)).isFalse();
    }

    @Test
    void addFavorite() {
        Favorite favorite = new Favorite(1L, 1L, 2L);
        member.addFavorite(favorite);
        assertThat(member.getFavorites()).hasSize(1);
    }
}