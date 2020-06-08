package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "kim@naver.com", "KDB", "123");
        member.addFavorite(new Favorite(1L, 3L));
        member.addFavorite(new Favorite(2L, 4L));
    }

    @Test
    @DisplayName("즐겨찾기를 추가하는 기능 테스트")
    void addFavorite() {
        member.addFavorite(new Favorite(4L, 5L));
        assertTrue(member.getFavorites().contains(new Favorite(4L, 5L)));
    }

    @Test
    @DisplayName("즐겨찾기를 삭제하는 기능 테스트")
    void removeFavorite() {
        member.removeFavorite(Favorite.of(1L, 3L));
        assertFalse(member.getFavorites().contains(new Favorite(1L, 3L)));
    }

    @Test
    @DisplayName("없는 즐겨찾기를 삭제 시도할 경우 테스트")
    void removeFavoriteException() {
        assertThatThrownBy(() -> member.removeFavorite(Favorite.of(8L, 9L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되어 있지 않은");
    }
}
