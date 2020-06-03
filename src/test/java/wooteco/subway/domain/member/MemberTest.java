package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.web.exception.NoSuchValueException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    private Member member;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        this.member = new Member(1L, "aa@aa.com", "또링", "1234");
        this.favorite = new Favorite(1L, 1L, 2L);
    }

    @Test
    void update() {
        member.update("엘리", "5678");
        assertThat(member.getName()).isEqualTo("엘리");
        assertThat(member.getPassword()).isEqualTo("5678");
    }

    @Test
    void checkPassword() {
        assertThat(member.checkPassword("1234")).isTrue();
        assertThat(member.checkPassword("5678")).isFalse();
    }

    @Test
    void addFavorite() {
        member.addFavorite(favorite);

        assertThat(member.getFavorites().contains(favorite)).isTrue();
        assertThat(member.getFavorites().size()).isEqualTo(1);
    }

    @Test
    void findFavorite() {
        member.addFavorite(favorite);
        assertThat(
                member.findFavorite(favorite.getDepartureId(), favorite.getDestinationId())).isEqualTo(
                favorite);
        assertThatThrownBy(() -> member.findFavorite(100L, 200L))
                .isInstanceOf(NoSuchValueException.class);
    }

    @Test
    void deleteFavorite() {
        member.addFavorite(favorite);
        member.deleteFavorite(favorite.getId());

        assertThat(member.getFavorites().contains(favorite)).isFalse();
        assertThat(member.getFavorites().size()).isEqualTo(0);

    }
}