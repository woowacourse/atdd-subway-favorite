package wooteco.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    /*
    시나리오
    회원을 생성한다.
    역들을 생성한다.
    생성된 회원으로 로그인한다.
    로그인하고 발급받은 토큰으로 즐겨찾기를 추가한다.
    로그인하고 발급받은 토큰으로 즐겨찾기 목록을 조회한다.
    즐겨찾기 목록이 조회되는지 확인한다.
    로그인하고 발급받은 토큰으로 즐겨찾기를 삭제한다.
    즐겨찾기가 삭제된 것을 확인한다.
     */

    @DisplayName("즐겨찾기 목록을 관리한다.")
    @Test
    void manageFavorite() {
        //회원을 생성한다.
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        //역들을 생성한다.
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_DOGOK);
        createStation(STATION_NAME_SEOLLEUNG);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_HANTI);
        createStation(STATION_NAME_MAEBONG);

        //생성된 회원으로 로그인한다.
        TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        //로그인하고 발급받은 토큰으로 즐겨찾기를 추가한다.
        addFavorite(token, STATION_NAME_KANGNAM, STATION_NAME_DOGOK);
        addFavorite(token, STATION_NAME_SEOLLEUNG, STATION_NAME_YEOKSAM);
        addFavorite(token, STATION_NAME_HANTI, STATION_NAME_MAEBONG);
        //로그인하고 발급받은 토큰으로 즐겨찾기 목록을 조회한다.
        List<FavoriteResponse> favoriteResponses = getOwnFavorites(token);
        //즐겨찾기 목록이 조회되는지 확인한다.
        assertThat(favoriteResponses).hasSize(3);
        assertThat(favoriteResponses.get(0).getId()).isEqualTo(1L);
        assertThat(favoriteResponses.get(1).getId()).isEqualTo(2L);
        assertThat(favoriteResponses.get(2).getId()).isEqualTo(3L);

    }
}
