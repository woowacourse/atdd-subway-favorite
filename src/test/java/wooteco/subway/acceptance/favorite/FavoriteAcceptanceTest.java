package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("즐겨찾기를 관리(추가, 조회, 삭제) 한다.")
    @Test
    public void manageFavorites() {
        // given(line, station, lineStation, member, login)
        StationResponse 강남역 = createStation(STATION_NAME_KANGNAM);
        StationResponse 역삼역 = createStation(STATION_NAME_YEOKSAM);
        StationResponse 선릉역 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse 이호선 = createLine("2호선");

        addLineStation(이호선.getId(), null, 강남역.getId());
        addLineStation(이호선.getId(), 강남역.getId(), 역삼역.getId());
        addLineStation(이호선.getId(), 역삼역.getId(), 선릉역.getId());

        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // when : 즐겨찾기 추가
        addMyFavorite(token, 강남역.getId(), 선릉역.getId());
        // then : 즐겨찾기 조회
        List<FavoriteResponse> favorites = getAllMyFavorites(token);
        assertThat(favorites.get(0).getSourceStationId()).isEqualTo(강남역.getId());
        assertThat(favorites.get(0).getTargetStationId()).isEqualTo(선릉역.getId());

        // when : 즐겨찾기 삭제
        removeFavorite(token, favorites.get(0).getId());
        // then : 즐겨찾기 조회
        favorites = getAllMyFavorites(token);
        assertThat(favorites.size()).isEqualTo(0);
    }

    private void addMyFavorite(final TokenResponse token, final Long source, final Long target) {
        FavoriteRequest request = new FavoriteRequest(source, target);

        given().
                header("Authorization", token.getTokenType() + " " + token.getAccessToken()).
                body(request).
                accept(MediaType.APPLICATION_JSON_VALUE).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/favorites").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }

    private List<FavoriteResponse> getAllMyFavorites(final TokenResponse token) {
        return given().
                header("Authorization", token.getTokenType() + " " + token.getAccessToken()).
                when().
                get("/favorites").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().
                jsonPath().getList(".", FavoriteResponse.class);
    }

    private void removeFavorite(final TokenResponse token, final Long id) {
        given().
                header("Authorization", token.getTokenType() + " " + token.getAccessToken()).
                when().
                delete("/favorites/" + id).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());

    }

}
