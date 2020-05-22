package wooteco.subway.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.member.favorite.Favorite;
import wooteco.subway.web.member.exception.MemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    private Member member;

    @DisplayName("사용자 생성시 즐겨찾기 목록이 없다.")
    @Test
    void member() {
        member = new Member(1L, "example@gmail.com", "박찬인", "example");

        assertThat(member.getFavorites().getFavorites()).hasSize(0);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        //given
        member = new Member(1L, "example@gmail.com", "박찬인", "example");
        Favorite favorite = new Favorite(1L, 2L);
        //when
        member.addFavorite(favorite);
        //then
        assertThat(member.getFavorites().getFavorites()).hasSize(1);
        assertThat(member.getFavorites().getFavorites().get(0).getSourceStationId()).isEqualTo(1L);
        assertThat(member.getFavorites().getFavorites().get(0).getTargetStationId()).isEqualTo(2L);
    }

    @DisplayName("이미 존재하는 즐겨찾기 목록을 추가할 경우 예외 처리")
    @Test
    void addDuplicatedFavorite() {
        //given
        member = new Member(1L, "example@gmail.com", "박찬인", "example");
        Favorite favorite = new Favorite(1L, 2L);
        member.addFavorite(favorite);
        //then
        assertThatThrownBy(() -> member.addFavorite(favorite))
                .isInstanceOf(MemberException.class)
                .hasMessage("source: %d, target: %d, 이미 존재하는 즐겨찾기입니다.", favorite.getSourceStationId(), favorite.getTargetStationId());
    }

    @DisplayName("즐겨찾기 제거")
    @Test
    void removeFavorite() {
        //given
        member = new Member(1L, "example@gmail.com", "박찬인", "example");
        Favorite favorite = new Favorite(1L, 2L);
        member.addFavorite(favorite);
        //when
        member.removeFavorite(favorite);
        //then
        assertThat(member.getFavorites().getFavorites()).hasSize(0);
    }

    @Test
    void removeNotFoundFavorite() {
        member = new Member(1L, "example@gmail.com", "박찬인", "example");
        Favorite favorite = new Favorite(1L, 2L);
        //then
        assertThatThrownBy(() -> member.removeFavorite(favorite))
                .isInstanceOf(MemberException.class)
                .hasMessage("source: %d, target: %d, 존재하지 않는 즐겨찾기입니다.", favorite.getSourceStationId(), favorite.getTargetStationId());
    }
}