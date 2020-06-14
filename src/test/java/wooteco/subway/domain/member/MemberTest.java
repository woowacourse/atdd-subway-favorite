package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.station.Station;

class MemberTest {

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_NAME = "testName";
    private static final String TEST_PASSWORD = "test1234";

    private Member member;

    @BeforeEach
    void setUp() {
        this.member = new Member(TEST_EMAIL, TEST_NAME, TEST_PASSWORD);
    }

    @Test
    void update() {
        final String newName = "newName";
        final String newPassword = "newPassword";
        member.update(newName, newPassword);

        assertThat(member.getName()).isEqualTo(newName);
        assertThat(member.checkPassword(newPassword)).isTrue();
    }

    @Test
    void checkPassword() {
        assertThat(member.checkPassword(TEST_PASSWORD)).isTrue();
        assertThat(member.checkPassword("weird password")).isFalse();
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
        FavoritePath favoritePath = new FavoritePath(start, end);
        member.addFavoritePath(favoritePath);
        final int givenSize = member.getFavoritePaths().size();

        // when : member 의 removeFavoritePath 인스턴스메서드를 호출하여 A를 삭제 시도한다.
        member.removeFavoritePath(start, end);

        // then : member 객체에서 favorite path A 가 삭제된다.
        assertThat(member.getFavoritePaths()).hasSize(givenSize - 1);
    }

}