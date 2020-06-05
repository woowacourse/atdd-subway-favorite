package wooteco.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

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

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 관리(추가, 조회, 삭제) 인수테스트")
    @Test
    public void manageFavorite() {
        //given 회원가입이 되어 있고, 역/노선/구간이 등록되어 있다
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        //and 로그인이 되어 있다
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        //when 즐겨찾기 추가를 한다
        addFavoritePath(tokenResponse, stationResponse1, stationResponse2);

        //when 즐겨찾기 조회 요청을 한다
        List<FavoriteResponse> responses = getFavoritePath(tokenResponse);

        //then 즐겨찾기가 추가되고 조회할 수 있다
        assertThat(responses.get(0).getSourceStationId()).isEqualTo(stationResponse1.getId());
        assertThat(responses.get(0).getTargetStationId()).isEqualTo(stationResponse2.getId());

        //when 즐겨찾기 삭제 요청을 한다
        deleteFavoritePath(tokenResponse, responses.get(0).getId());

        //then 즐겨찾기가 삭제된다
        responses = getFavoritePath(tokenResponse);
        assertThat(responses.size()).isEqualTo(0);
    }

    private void addFavoritePath(
        TokenResponse tokenResponse,
        StationResponse source,
        StationResponse target
    ) {
        FavoriteRequest request = new FavoriteRequest(source.getId(), target.getId());
        given().
            body(request).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            header("Authorization",tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
        when().
            post("/favorites").
        then().
            log().all().
            statusCode(HttpStatus.CREATED.value());
    }

    private List<FavoriteResponse> getFavoritePath(TokenResponse tokenResponse) {
        return given().
                    header("Authorization",tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                when().
                    get("/favorites").
                then().
                    log().all().
                    extract().
                    jsonPath().getList(".", FavoriteResponse.class);
    }

    private void deleteFavoritePath(TokenResponse tokenResponse, Long favoriteId) {
        given().
            header("Authorization",tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
            when().
            delete("/favorites/" + favoriteId).
            then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }

}
