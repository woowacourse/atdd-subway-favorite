package wooteco.subway.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.station.Station;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemberTest {

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_NAME = "testName";
    private static final String TEST_PASSWORD = "test1234";

    private Member member;

    @BeforeEach
    void setUp() {
        this.member = new Member(TEST_EMAIL, TEST_NAME, TEST_PASSWORD);
    }

    @DisplayName("즐겨찾기 경로 추가")
    @Test
    void addFavoritePath() {
        Station start = new Station(1L, "신정역");
        Station end = new Station(2L, "목동역");
        FavoritePath favoritePath = new FavoritePath(start, end);

        member.addFavoritePath(favoritePath);

        Set<FavoritePath> favoritePaths = member.getFavoritePaths();
        assertThat(favoritePaths).hasSize(1);
    }

    @DisplayName("즐겨찾기 경로 삭제")
    @Test
    void removeFavoritePath() {
        // given : member 객체에 favorite path A 가 저장되어있다.
        Station start = new Station(1L, "신정역");
        Station end = new Station(2L, "목동역");
        FavoritePath favoritePath = mock(FavoritePath.class);
        when(favoritePath.match(start, end)).thenReturn(true);
        member.addFavoritePath(favoritePath);
        final int givenSize = member.getFavoritePaths().size();

        // when : member 의 removeFavoritePath 인스턴스메서드를 호출하여 A를 삭제 시도한다.
        member.removeFavoritePath(start, end);

        // then : member 객체에서 favorite path A 가 삭제된다.
        assertThat(member.getFavoritePaths()).hasSize(givenSize - 1);
    }

    @DisplayName("즐겨찾기를 가지고 있는지 확인")
    @Test
    void has() {
        Station start = new Station(1L, "신정역");
        Station end = new Station(2L, "목동역");
        FavoritePath favoritePath = new FavoritePath(start, end);
        assertThat(member.has(favoritePath)).isEqualTo(false);
        member.addFavoritePath(favoritePath);
        assertThat(member.has(new FavoritePath(start, end))).isEqualTo(true);
    }


}