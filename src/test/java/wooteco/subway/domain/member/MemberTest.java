package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.exception.DuplicateFavoriteException;

class MemberTest {

    @DisplayName("예외테스트: 이미 존재하는 즐겨찾기를 추가하는 경우")
    @Test
    void addFavorite_withExistingFavorite() {
        //given
        Member member = new Member();
        Favorite favorite = new Favorite(1L, 2L);
        member.addFavorite(favorite);

        //when, then
        assertThatThrownBy(() -> member.addFavorite(favorite))
            .isInstanceOf(DuplicateFavoriteException.class)
            .hasMessage("이미 존재하는 즐겨찾기를 추가할 수 없습니다.");
    }

}
