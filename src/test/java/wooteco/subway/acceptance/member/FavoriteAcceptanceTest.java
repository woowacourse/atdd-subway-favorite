package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

/**
 * <br/>Scenario: 노선 즐겨찾기 관리
 *<br/>
 * <br/>given 지하철역들이 추가 되어있다.
 * <br/>and   노선이 추가 되어있다.
 * <br/>and   노선에 지하철역들이 추가되어있다.
 * <br/>and   사용자가 추가 되어있다.
 * <br/>and   사용자가 로그인 되어있다. (토큰)
 * <br/>and   사용자가 지하철 경로를 검색했다.
 *<br/>
 * <br/>given 사용자가 로그인되어있다.
 * <br/>when  사용자가 즐겨찾기를 추가한다.
 * <br/>then  즐겨찾기가 추가된다.
 *<br/>
 * <br/>given 즐겨찾기가 추가되어있다.
 * <br/>and   사용자가 로그인되어있다.
 * <br/>when  사용자가 즐겨찾기를 조회한다.
 * <br/>then  즐겨찾기 목록이 출력된다.
 *<br/>
 * <br/>given 즐겨찾기가 추가되어있다.
 * <br/>and   사용자가 로그인되어있다.
 * <br/>when  사용자가 즐겨찾기를 삭제한다.
 * <br/>then  즐겨찾기가 삭제된다.
 *<br/>
 * <br/>maybe 지하철역/노선이 존재하지 않는 경우
 * <br/>maybe 노선이 이어지지 않는 경우
 * <br/>maybe 시작과 끝이 같은 경우
 * <br/>maybe 이미 존재하는 즐겨찾기의 경우 (시작, 끝이 같음)에는 표시를
 *<br/>
 * <br/>maybe 지하철역 / 노선이 삭제된 경우 즐겨찾기가 삭제됨
 * <br/>maybe 회원 탈퇴의 경우 즐겨찾기가 삭제됨
 *
 */
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() throws Exception {
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");
        StationResponse station3 = createStation("station3");

        LineResponse line1 = createLine("line1");
        addLineStation(line1.getId(), null, station1.getId());
        addLineStation(line1.getId(), station1.getId(), station2.getId());
        addLineStation(line1.getId(), station2.getId(), station3.getId());

        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        createMember(TEST_USER_EMAIL2, TEST_USER_NAME2, TEST_USER_PASSWORD2);

        // when
        FavoriteRequest request = new FavoriteRequest(station1.getId(), station3.getId());
        TokenResponse token1 = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        createFavorite(request, token1);

        List<FavoriteResponse> favorites = getAllFavorites(token1);
        Long createdId = favorites.get(0).getId();

        // then
        assertThat(favorites).isNotNull();
        assertThat(createdId).isNotNull();
        assertThat(favorites.get(0).getSourceStationId()).isEqualTo(station1.getId());
        assertThat(favorites.get(0).getSourceStationName()).isEqualTo(station1.getName());
        assertThat(favorites.get(0).getTargetStationId()).isEqualTo(station3.getId());
        assertThat(favorites.get(0).getTargetStationName()).isEqualTo(station3.getName());

        // when
        deleteFavorite(createdId, token1);

        // then
        List<FavoriteResponse> favoritesAfterDelete = getAllFavorites(token1);
        assertThat(favoritesAfterDelete).isEmpty();
    }
}
