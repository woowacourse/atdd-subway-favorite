package wooteco.subway.domain.member;

import org.junit.jupiter.api.Test;
import wooteco.subway.domain.favorite.Favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.DummyTestUserInfo.*;

class MemberTest {
    @Test
    void addFavorite() {
        Member member = new Member(EMAIL, NAME, PASSWORD);
        member.addFavorite(new Favorite(1L, 2L));
        assertThat(member.getFavorites().size()).isEqualTo(1);
    }


}