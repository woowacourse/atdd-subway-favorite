package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        assertTrue(member.getFavorites().getFavorites().contains(new Favorite(4L, 5L)));
    }

    @Test
    @DisplayName("즐겨찾기를 삭제하는 기능 테스트")
    void removeFavorite() {
        member.removeFavorite(new Favorite(1L, 3L));
        assertFalse(member.getFavorites().getFavorites().contains(new Favorite(1L, 3L)));
    }
}
