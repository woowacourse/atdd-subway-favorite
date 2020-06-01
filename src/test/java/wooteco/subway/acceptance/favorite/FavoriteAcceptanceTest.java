package wooteco.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.AuthAcceptanceTest.*;
import static wooteco.subway.acceptance.member.MyInfoAcceptanceTest.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 추가 / 목록 조회 / 삭제")
    @Test
    void manageFavorite() {
        LineResponse line = createLine("1호선");
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        addLineStation(line.getId(), null, stationResponse1.getId());
        addLineStation(line.getId(), stationResponse1.getId(), stationResponse2.getId());
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = successLogin(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        String location = addFavorite(tokenResponse, stationResponse1.getId(),
            stationResponse2.getId());

        FavoriteResponse favoriteResponse = getFavorite(tokenResponse, stationResponse1.getId(),
            stationResponse2.getId());

        assertThat(favoriteResponse).isNotNull();

        List<FavoriteResponse> favorites = getFavorites(tokenResponse);
        assertThat(favorites).size().isEqualTo(1);

        removeFavorite(tokenResponse, stationResponse1.getId(), stationResponse2.getId());
    }

    private String addFavorite(TokenResponse tokenResponse, Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return given().auth().
            oauth2(tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            post("/favorites").
            then().
            statusCode(HttpStatus.CREATED.value()).
            extract().header("Location");
    }

    private FavoriteResponse getFavorite(TokenResponse tokenResponse, Long source, Long target) {
        return given().auth().
            oauth2(tokenResponse.getAccessToken()).
            when().
            get("/favorites/source/" + source + "/target/" + target).
            then().
            statusCode(HttpStatus.OK.value()).
            extract().as(FavoriteResponse.class);
    }

    private List<FavoriteResponse> getFavorites(TokenResponse tokenResponse) {
        return given().auth().
            oauth2(tokenResponse.getAccessToken()).
            when().
            get("/favorites").
            then().
            statusCode(HttpStatus.OK.value()).
            extract().
            jsonPath().
            getList(".", FavoriteResponse.class);

    }

    private void removeFavorite(TokenResponse tokenResponse, Long source, Long target) {
        given().auth().
            oauth2(tokenResponse.getAccessToken()).
            when().
            delete("/favorites/source/" + source + "/target/" + target).
            then().
            statusCode(HttpStatus.NO_CONTENT.value());
    }
}
