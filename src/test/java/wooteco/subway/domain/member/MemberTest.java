package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.CustomException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        assertThatThrownBy(() -> member.addFavorite(favorite))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 존재하는 즐겨찾기입니다.");
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
        assertThatThrownBy(() -> member.removeFavorite(new Favorite("잠실역", "잠실새내역")))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 즐겨찾기입니다.");
    }
}