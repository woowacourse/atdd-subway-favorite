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

    }

    @Test
    void checkPassword() {
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
}