package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {
    private static final String SOURCE = "강남역";
    private static final String TARGET = "삼성역";

    private Member member;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        member = new Member("origin@naver.com", "name", "password");
        favorite = new Favorite(SOURCE, TARGET);
    }

    @Test
    void update() {
        String updatedName = "updatedName";
        String updatedPassword = "updatedPassword";

        member.update(updatedName, updatedPassword);

        assertThat(member.getName()).isEqualTo(updatedName);
        assertThat(member.getPassword()).isEqualTo(updatedPassword);
    }

    @Test
    void addFavortie() {
        member.addFavorite(favorite);

        assertThat(member.getFavorites()).hasSize(1);
    }

    @Test
    void addFavorite_AlreadyExist() {
        member.addFavorite(favorite);

        assertThrows(IllegalArgumentException.class, () -> member.addFavorite(favorite));
    }

    @Test
    void removeFavorite() {
        member.addFavorite(favorite);
        member.removeFavorite(favorite);
        assertThat(member.getFavorites()).isEmpty();
    }

    @Test
    void removeFavorite_NotExist() {
        member.addFavorite(favorite);
        assertThrows(IllegalArgumentException.class,
                () -> member.removeFavorite(new Favorite("잠실역", "잠실새내역")));
    }
}