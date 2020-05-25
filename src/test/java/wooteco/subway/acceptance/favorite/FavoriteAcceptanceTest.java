package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("즐겨찾기 목록을 관리한다")
    @Test
    void manageFavorite() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        createFavorite(1L, STATION_NAME_KANGNAM, STATION_NAME_DOGOK);
    }
}
