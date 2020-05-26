package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.favorite.Favorite;

public class MemberTest {
    private Member member;

    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    @BeforeEach
    void setUp() {
        member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void addFavorite() {
        // when
        member.addFavorite(new Favorite(1L, 2L));

        // then
        assertThat(member.getFavorites().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("즐겨찾기 중복 추가")
    void addFavoriteTwice() {
        // when
        Favorite favorite = new Favorite(1L, 2L);
        member.addFavorite(favorite);
        member.addFavorite(favorite);

        // then
        assertThat(member.getFavorites().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void removeFavorite() {
        // given
        Favorite favorite = new Favorite(1L, 2L);
        member.addFavorite(favorite);

        // when
        member.removeFavorite(favorite);

        // then
        assertThat(member.getFavorites().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("즐겨찾기 존재 여부")
    void hasFavorite() {
        // when
        Favorite favorite = new Favorite(1L, 2L);
        member.addFavorite(favorite);

        // then
        assertThat(member.hasFavorite(favorite)).isTrue();
        member.removeFavorite(favorite);
        assertThat(member.hasFavorite(favorite)).isFalse();
    }
}


