package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.member.favorite.Favorite;
import wooteco.subway.domain.station.Station;
import wooteco.subway.web.member.exception.MemberException;

class MemberTest {
    private Member member;
    private Station source;
    private Station target;

    @BeforeEach
    void setUp() {
        source = new Station(1L, "잠실역");
        target = new Station(2L, "역삼역");
    }

    @DisplayName("사용자 생성시 즐겨찾기 목록이 없다.")
    @Test
    void member() {
        member = new Member(1L, "example@gmail.com", "박찬인", "example");

        assertThat(member.getFavorites().getValues()).hasSize(0);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        //given
        member = new Member(1L, "example@gmail.com", "박찬인", "example");
        Favorite favorite = new Favorite(1L, member, source, target);
        //when
        member.addFavorite(favorite);
        //then
        assertThat(member.getFavorites().getValues()).hasSize(1);
        assertThat(
            member.getFavorites().getValues().get(0).getSourceStation().getId()).isEqualTo(1L);
        assertThat(
            member.getFavorites().getValues().get(0).getTargetStation().getId()).isEqualTo(2L);
    }

    @DisplayName("이미 존재하는 즐겨찾기 목록을 추가할 경우 예외 처리")
    @Test
    void addDuplicatedFavorite() {
        //given
        member = new Member(1L, "example@gmail.com", "박찬인", "example");
        Favorite favorite = new Favorite(member, source, target);
        member.addFavorite(favorite);
        //then
        assertThatThrownBy(() -> member.addFavorite(favorite))
            .isInstanceOf(MemberException.class)
            .hasMessage("source: %s, target: %s, 이미 존재하는 즐겨찾기입니다.",
                favorite.getSourceStation().getName(),
                favorite.getTargetStation().getName());
    }

    @DisplayName("즐겨찾기 제거")
    @Test
    void removeFavorite() {
        //given
        member = new Member(1L, "example@gmail.com", "박찬인", "example");
        Favorite favorite = new Favorite(member, source, target);
        member.addFavorite(favorite);
        //when
        member.removeFavorite(favorite);
        //then
        assertThat(member.getFavorites().getValues()).hasSize(0);
    }

    @DisplayName("존재하지 않는 즐겨찾기 제거시 Exception 발생")
    @Test
    void removeNotFoundFavorite() {
        member = new Member(1L, "example@gmail.com", "박찬인", "example");
        Favorite favorite = new Favorite(member, source, target);
        //then
        assertThatThrownBy(() -> member.removeFavorite(favorite))
            .isInstanceOf(MemberException.class)
            .hasMessage("source: %s, target: %s, 존재하지 않는 즐겨찾기입니다.",
                favorite.getSourceStation().getName(),
                favorite.getTargetStation().getName());
    }
}