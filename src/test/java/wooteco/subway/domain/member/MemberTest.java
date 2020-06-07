package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class MemberTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("brown@email.com", "brown", "1234");
    }

    @Test
    void checkPasswordTest() {
        assertThat(member.checkPassword("5")).isFalse();
    }

    @Test
    void deleteFavoriteByIdTEst() {
        member.addFavorite(new Favorite(1L, 1L, 2L));
        member.addFavorite(new Favorite(2L, 2L, 3L));
        member.addFavorite(new Favorite(3L, 3L, 4L));

        member.deleteFavoriteBy(2L);

        assertThat(member.getFavorites().size()).isEqualTo(2);
        assertThat(member.getFavorites().stream()
                .map(Favorite::getId)
                .collect(Collectors.toSet()).contains(2L)).isFalse();
    }

    @Test
    void updateTest() {
        member.update("newName","");

        assertThat(member.getName()).isEqualTo("newName");
        assertThat(member.getPassword()).isEqualTo("1234");
    }
}
